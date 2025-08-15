package msa.productservice.application.port.in;

import jakarta.annotation.Nullable;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.adapter.in.web.dto.ProductWithClientDto;
import msa.productservice.adapter.in.web.dto.SearchAfterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductQueryUseCase {
    List<ProductWithClientDto> getProductsWithClientName(); //feign 사용
    Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size); //elasticsearch 사용
    Page<ProductWithClientDto> getProductsWithClients(Long clientCode, String keyword, Pageable pageable); //RDB JOIN
    SearchAfterResponse searchAfter(Long clientCode, String keyword, int size, @Nullable String afterToken);
}