package msa.productservice.application.port.in;

import msa.productservice.adapter.in.web.dto.ProductCreateRequest;
import msa.productservice.adapter.in.web.dto.ProductResponse;

public interface ProductCommandUseCase {
    ProductResponse registerProduct(ProductCreateRequest productCreateRequest, String UserID, String roleName);
}
