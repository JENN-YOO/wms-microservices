package msa.productservice.adapter.out.search;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.application.port.out.ProductSearchIndexPort;
import msa.productservice.application.port.out.ProductSearchQueryPort;
import msa.productservice.adapter.out.persistence.ClientMasterRepository;
import msa.productservice.adapter.out.persistence.ProductMasterRepository;
import msa.productservice.config.MDCHelper;
import msa.productservice.domain.ClientMaster;
import msa.productservice.domain.ProductMaster;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchProductSearchAdapter
        implements ProductSearchIndexPort, ProductSearchQueryPort {

    private final ProductMasterRepository productRepo;
    private final ClientMasterRepository clientRepo;
    private final ProductSearchESRepository esRepository;
    private final ElasticsearchOperations operations;

    private IndexCoordinates index() {
        return operations.getIndexCoordinatesFor(ProductSearchDoc.class);
    }

    // ===== 색인 (상품 단건 업서트) =====
    @Override
    public void upsertByProductCode(Long productCode, Long eventVersion) {
        Instant start = Instant.now();
        debug("UPsert 시작 - productCode=" + productCode + ", eventVersion=" + eventVersion);

        try {
            var existing = esRepository.findById(productCode).orElse(null);
            if (existing != null && existing.getVersion() != null
                    && existing.getVersion() >= eventVersion) {
                debug("이미 최신 버전 존재 - skip (existingVersion=" + existing.getVersion() + ")");
                return;
            }

            ProductMaster p = productRepo.findById(productCode)
                    .orElseThrow(() -> new IllegalStateException("Product not found: " + productCode));
            Long clientCode = Long.valueOf(p.getClientCode());
            ClientMaster c = clientRepo.findById(clientCode).orElse(null);

            ProductSearchDoc doc = ProductSearchDoc.builder()
                    .productCode(p.getProductCode())
                    .clientCode(clientCode)
                    .clientName(c != null ? c.getClientName() : null)
                    .productName(p.getProductName())
                    .brand(p.getBrand())
                    .style(p.getStyle())
                    .color(p.getColor())
                    .size(p.getSize())
                    .productYear(p.getProductYear())
                    .productSeason(p.getProductSeason())
                    .retailPrice(p.getRetailPrice())
                    .useYn(p.getUseYn())
                    .keywords(buildKeywords(p))
                    .lastEventAt(Instant.now())
                    .version(eventVersion)
                    .build();

            esRepository.save(doc); // upsert
            debug("ES 저장 완료 - productCode=" + productCode);
        } catch (Exception e) {
            debug("ES 저장 실패 - productCode=" + productCode + ", error=" + e.getMessage());
            throw e;
        } finally {
            debug("UPsert 종료 - productCode=" + productCode + ", elapsedMs=" + elapsedMs(start));
        }
    }

    private String buildKeywords(ProductMaster p) {
        StringBuilder sb = new StringBuilder();
        append(sb, p.getProductName());
        append(sb, p.getBrand());
        append(sb, p.getStyle());
        append(sb, p.getColor());
        append(sb, p.getSize());
        String kw = sb.toString().trim();
        debug("키워드 생성 - productCode=" + p.getProductCode() + ", keywords=\"" + kw + "\"");
        return kw;
    }
    private void append(StringBuilder sb, String v) { if (v != null && !v.isBlank()) sb.append(v).append(' '); }

    // ===== 색인 (화주 변경 → 해당 상품 문서들 부분 업데이트) =====
    @Override
    public void updateClientFields(Long clientCode) {
        Instant start = Instant.now();
        debug("화주필드 업데이트 시작 - clientCode=" + clientCode);

        var productCodes = productRepo.findProductCodesByClientCode(clientCode);
        var client = clientRepo.findById(clientCode).orElse(null);
        String newClientName = client != null ? client.getClientName() : null;

        debug("대상 상품수=" + productCodes.size() + ", newClientName=" + newClientName);

        if (productCodes.isEmpty()) {
            debug("업데이트 대상 없음 - clientCode=" + clientCode);
            return;
        }

        int updated = 0;
        for (Long pc : productCodes) {
            var existing = esRepository.findById(pc).orElse(null);
            if (existing == null) continue;
            if (!Objects.equals(existing.getClientName(), newClientName)) {
                existing.setClientName(newClientName);
                existing.setLastEventAt(Instant.now());
                esRepository.save(existing);
                updated++;
            }
        }
        debug("화주필드 업데이트 완료 - clientCode=" + clientCode + ", updatedCount=" + updated +
                ", elapsedMs=" + elapsedMs(start));
    }

    // ===== 조회 (최적화: _source include, track_total_hits, minScore) =====
    @Override
    public Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size) {
        Instant start = Instant.now();
        int p = Math.max(page, 0);
        int s = Math.min(Math.max(size, 1), 100);

        String[] includes = {
                "productCode","clientCode","clientName",
                "productName","brand","style","color","size",
                "retailPrice"
        };

        var bool = QueryBuilders.bool();
        if (clientCode != null) {
            bool.must(m -> m.term(t -> t.field("clientCode").value(clientCode)));
        }
        if (keyword != null && !keyword.isBlank()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(keyword)
                    .fields("productName^3","brand^2","keywords")
            ));
        }

        var builder = NativeQuery.builder()
                .withQuery(bool.build()._toQuery())
                .withPageable(PageRequest.of(p, s, Sort.by(Sort.Order.asc("productCode"))))
                .withSourceFilter(new FetchSourceFilter(includes, null))
                .withTrackTotalHits(false);

        if (keyword != null && !keyword.isBlank()) {
            builder.withMinScore(0.01f);
        }

        Query q = builder.build();

        SearchHits<ProductSearchDoc> hits = operations.search(q, ProductSearchDoc.class, index());

        var mapped = hits.getSearchHits().stream().map(h -> {
            var d = h.getContent();
            return ProductSearchResponse.builder()
                    .productCode(d.getProductCode())
                    .clientCode(d.getClientCode())
                    .clientName(d.getClientName())
                    .productName(d.getProductName())
                    .brand(d.getBrand())
                    .style(d.getStyle())
                    .color(d.getColor())
                    .size(d.getSize())
                    .retailPrice(d.getRetailPrice())
                    .build();
        }).toList();

        debug("ES 응답 - returned=" + mapped.size() + ", page=" + p + ", size=" + s + ", elapsedMs=" + elapsedMs(start));
        return new PageImpl<>(mapped, q.getPageable(), hits.getTotalHits());
    }

    // ===== 딥 페이지네이션 (search_after) =====
    public static class SearchPage<T> {
        private final List<T> contents;
        private final List<Object> nextToken;
        public SearchPage(List<T> contents, List<Object> nextToken) {
            this.contents = contents; this.nextToken = nextToken;
        }
        public List<T> getContents() { return contents; }
        public List<Object> getNextToken() { return nextToken; }
    }

    public SearchPage<ProductSearchResponse> searchAfter(
            Long clientCode, String keyword, int size, @Nullable List<Object> afterSortValues) {

        int s = Math.min(Math.max(size, 1), 100);

        // 정렬: productCode만 (keyword/number 모두 안전)
        // 만약 유니크가 확실하면 이 한 개로 충분
        // 나중에 필요하면 createdAt 같은 2차키를 추가
        Sort sort = Sort.by(Sort.Order.asc("productCode"));

        var bool = QueryBuilders.bool();
        if (clientCode != null) {
            bool.must(m -> m.term(t -> t.field("clientCode").value(clientCode)));
        }
        if (keyword != null && !keyword.isBlank()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(keyword)
                    .fields("productName^3","brand^2") // keywords 매핑 불확실 → 일단 제외
            ));
        }

        String[] includes = {
                "productCode","clientCode","clientName",
                "productName","brand","style","color","size",
                "retailPrice"
        };

        var builder = NativeQuery.builder()
                .withQuery(bool.build()._toQuery())
                .withSort(sort)
                .withPageable(PageRequest.of(0, s))   // search_after 사용 시 from=0 고정
                .withTrackTotalHits(false)
                .withSourceFilter(new FetchSourceFilter(includes, null));

        if (keyword != null && !keyword.isBlank()) {
            builder.withMinScore(0.01f);
        }

        if (afterSortValues != null && !afterSortValues.isEmpty()) {
            builder.withSearchAfter(afterSortValues); //그대로 전달
        }

        Query q = builder.build();

        try {
            var hits = operations.search(q, ProductSearchDoc.class, index());

            var items = hits.getSearchHits().stream().map(h -> {
                var d = h.getContent();
                return ProductSearchResponse.builder()
                        .productCode(d.getProductCode())
                        .clientCode(d.getClientCode())
                        .clientName(d.getClientName())
                        .productName(d.getProductName())
                        .brand(d.getBrand())
                        .style(d.getStyle())
                        .color(d.getColor())
                        .size(d.getSize())
                        .retailPrice(d.getRetailPrice())
                        .build();
            }).collect(java.util.stream.Collectors.toList());

            List<Object> nextToken = null;
            var lastHit = hits.getSearchHits().isEmpty() ? null
                    : hits.getSearchHits().get(hits.getSearchHits().size() - 1);
            if (lastHit != null && lastHit.getSortValues() != null && !lastHit.getSortValues().isEmpty()) {
                nextToken = lastHit.getSortValues();
            }

            return new SearchPage<>(items, nextToken);

        } catch (org.springframework.dao.DataAccessException e) {
            // 루트 원인 로그 터지면 바로 찍히게
            Throwable root = e.getCause();
            if (root instanceof co.elastic.clients.elasticsearch._types.ElasticsearchException ee) {
                log.error("[ES] type={}, reason={}", ee.error().type(), ee.error().reason(), ee);
            } else {
                log.error("[ES] search failed", e);
            }
            throw e;
        }
    }




    private void debug(String msg) {
        MDCHelper.appendDebug(ElasticsearchProductSearchAdapter.class, msg);
        log.info("[ES-ADAPTER] {}", msg);
    }

    private static long elapsedMs(Instant start) {
        return java.time.Duration.between(start, Instant.now()).toMillis();
    }
}
