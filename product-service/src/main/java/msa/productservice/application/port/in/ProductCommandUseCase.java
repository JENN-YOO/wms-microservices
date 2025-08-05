package msa.productservice.application.port.in;

import msa.productservice.adapter.in.web.dto.ProductRequest;
import msa.productservice.adapter.in.web.dto.ProductResponse;

public interface ProductCommandUseCase {
    /**
     * 상품 등록
     * @param productRequest 상품 등록 요청 정보
     * @return 성공 여부와 메시지
     */
    ProductResponse registerProduct(ProductRequest productRequest, String UserID, String roleName);

    /**
     * 상품 수정
     * @param productId 상품 ID
     * @param productRequest 수정할 상품 정보
     * @return 성공 여부와 메시지
     */
//    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

    /**
     * 상품 삭제
     * @param productId 상품 ID
     * @return 성공 여부와 메시지
     */
//    ProductResponse deleteProduct(Long productId);


}
