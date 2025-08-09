package msa.productservice.application.port.out;

import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import org.springframework.data.domain.Page;

public interface ProductSearchQueryPort {
    Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size);
}
