package msa.productservice.application.port.in;

import msa.productservice.adapter.in.web.dto.ProductWithClientDto;

import java.util.List;

public interface ProductQueryUseCase {
    List<ProductWithClientDto> getProductsWithClientName();
}