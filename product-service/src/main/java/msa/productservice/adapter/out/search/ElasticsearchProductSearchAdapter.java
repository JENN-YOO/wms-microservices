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

import java.time.Instant;
import java.util.Objects;

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
    public void upsertByProductCode(Long productCode) {
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

        esRepository.save(doc); // upsert
    }

    private String buildKeywords(ProductMaster p) {
        StringBuilder sb = new StringBuilder();
        append(sb, p.getProductName());
        append(sb, p.getBrand());
        append(sb, p.getStyle());
        append(sb, p.getColor());
        append(sb, p.getSize());
        return sb.toString().trim();
    }
    private void append(StringBuilder sb, String v) { if (v != null && !v.isBlank()) sb.append(v).append(' '); }

    // ===== 색인 (화주 변경 → 해당 상품 문서들 부분 업데이트) =====
    @Override
    public void updateClientFields(Long clientCode) {
        var productCodes = productRepo.findProductCodesByClientCode(clientCode);
        var client = clientRepo.findById(clientCode).orElse(null);
        String newClientName = client != null ? client.getClientName() : null;

        if (productCodes.isEmpty()) return;

        // 간단 처리: 기존 문서 읽고 필드만 변경 후 save (대량이면 bulkUpdate로 전환)
        for (Long pc : productCodes) {
            var existing = esRepository.findById(pc).orElse(null);
            if (existing == null) continue;
            if (!Objects.equals(existing.getClientName(), newClientName)) {
                existing.setClientName(newClientName);
                existing.setLastEventAt(Instant.now());
                esRepository.save(existing);
            }
        }
    }

    // ===== 조회 =====
    @Override
    public Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("productCode")));

        // co.elastic QueryBuilders 사용
        var bool = QueryBuilders.bool();

        if (clientCode != null) {
            bool.must(m -> m.term(t -> t
                    .field("clientCode")
                    .value(clientCode)
            ));
        }

        if (keyword != null && !keyword.isBlank()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(keyword)
                    .fields("productName^3", "brand^2", "keywords")
            ));
            // 또는 simple_query_string 사용 가능:
            // bool.must(m -> m.simpleQueryString(s -> s.query(keyword).fields("productName^3","brand^2","keywords")));
        }

        Query q = NativeQuery.builder()
                .withQuery(bool.build()._toQuery())
                .withPageable(pageable)
                .build();

        SearchHits<ProductSearchDoc> hits = operations.search(q, ProductSearchDoc.class, index());
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

        return new PageImpl<>(mapped, pageable, hits.getTotalHits());
    }
}
