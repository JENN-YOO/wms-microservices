package msa.productservice.application.port.out;

public interface ProductSearchIndexPort {
    void upsertByProductCode(Long productCode, Long eventVersion);    // 상품 1건 단건 색인/업서트
    void updateClientFields(Long clientCode);     // 해당 화주 소속 상품 문서들 부분 업데이트
}
