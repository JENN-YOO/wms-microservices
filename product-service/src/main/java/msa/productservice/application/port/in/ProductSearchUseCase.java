package msa.productservice.application.port.in;

import org.springframework.data.domain.Page;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;

public interface ProductSearchUseCase {
    Page<ProductSearchResponse> search(ProductSearchCommand command);

    // 화주 이벤트 등으로 인덱스 갱신이 필요할 때
    void reindexByProductCode(Long productCode);
    void reindexByClientCode(Long clientCode);
}
