package msa.productservice.adapter.out.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.application.port.out.ProductSearchIndexPort;
import msa.productservice.application.port.out.ProductSearchQueryPort;
import msa.productservice.adapter.out.persistence.ClientMasterRepository;
import msa.productservice.adapter.out.persistence.ProductMasterRepository;
import msa.productservice.domain.ClientMaster;
import msa.productservice.domain.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

// ✅ MDC 헬퍼 import (너의 프로젝트 경로에 맞춰 수정)
import msa.productservice.config.MDCHelper;

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
    public void upsertByProductCode(Long productCode) {
        Instant start = Instant.now();
        debug("UPsert 시작 - productCode=" + productCode);

        try {
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
                    .version(0L)
                    .build();

            debug("ES 저장 시도 - productCode=" + productCode + ", clientCode=" + clientCode);
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

    // ===== 조회 =====
    @Override
    public Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size) {
        Instant start = Instant.now();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("productCode")));

        // co.elastic QueryBuilders 사용
        var bool = QueryBuilders.bool();

        if (clientCode != null) {
            bool.must(m -> m.term(t -> t.field("clientCode").value(clientCode)));
        }

        if (keyword != null && !keyword.isBlank()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(keyword)
                    .fields("productName^3", "brand^2", "keywords")
            ));
            // simple_query_string 사용 예:
            // bool.must(m -> m.simpleQueryString(s -> s.query(keyword).fields("productName^3","brand^2","keywords")));
        }

        Query q = NativeQuery.builder()
                .withQuery(bool.build()._toQuery())
                .withPageable(pageable)
                .build();

        debug("검색 요청 - clientCode=" + clientCode + ", keyword=\"" + keyword + "\"" +
                ", page=" + page + ", size=" + size);

        SearchHits<ProductSearchDoc> hits = operations.search(q, ProductSearchDoc.class, index());

        debug("ES 응답 - totalHits=" + hits.getTotalHits() +
                ", tookMs(approx)=" + elapsedMs(start));

        var mapped = hits.getSearchHits().stream()
                .map(h -> {
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
                })
                .toList();

        debug("검색 매핑 완료 - returned=" + mapped.size() +
                ", page=" + page + ", size=" + size +
                ", elapsedMs=" + elapsedMs(start));

        return new PageImpl<>(mapped, pageable, hits.getTotalHits());
    }

    private void debug(String msg) {
        MDCHelper.appendDebug(ElasticsearchProductSearchAdapter.class, msg);
        log.info("[ES-ADAPTER] {}", msg);
    }

    private static long elapsedMs(Instant start) {
        return Duration.between(start, Instant.now()).toMillis();
    }
}
