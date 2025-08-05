package msa.productservice.application.port.out;

import msa.productservice.domain.ProductMaster;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {
    /**
     * 상품을 저장합니다.
     *
     * @param product 상품 정보
     * @return 저장된 상품 정보
     */
    ProductMaster save(ProductMaster product);

    /**
     * 상품 ID로 상품을 조회합니다.
     *
     * @param productId 상품 ID
     * @return 조회된 상품 정보
     */
    Optional<ProductMaster> findById(Long productId);

    /**
     * 모든 상품을 조회합니다.
     *
     * @return 모든 상품 목록
     */
    List<ProductMaster> findAll();

    Optional<ProductMaster> findByProductName(String productName);
}
