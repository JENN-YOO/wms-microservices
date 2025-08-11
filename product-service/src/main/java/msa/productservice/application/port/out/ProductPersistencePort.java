package msa.productservice.application.port.out;

import msa.productservice.domain.ProductMaster;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {
    ProductMaster save(ProductMaster product);
    Optional<ProductMaster> findById(Long productId);
    List<ProductMaster> findAll();
    Optional<ProductMaster> findByProductName(String productName);
}
