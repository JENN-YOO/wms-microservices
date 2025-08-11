package msa.productservice.application.port.in;

import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.adapter.in.web.dto.ProductWithClientDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductQueryUseCase {
    List<ProductWithClientDto> getProductsWithClientName();
    Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size);
}