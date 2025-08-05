package msa.productservice.adapter.out.persistence;

import msa.productservice.application.port.out.ProductPersistencePort;
import msa.productservice.domain.ProductMaster;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductMasterPersistenceAdapter implements ProductPersistencePort {
    private final ProductMasterRepository productMasterRepository;

    public ProductMasterPersistenceAdapter(ProductMasterRepository productMasterRepository) {
        this.productMasterRepository = productMasterRepository;
    }

    @Override
    public ProductMaster save(ProductMaster product) {
        return productMasterRepository.save(product);
    }

    @Override
    public Optional<ProductMaster> findById(Long productId) {
        return productMasterRepository.findById(productId);
    }

    @Override
    public List<ProductMaster> findAll() {
        return productMasterRepository.findAll();
    }

    @Override
    public Optional<ProductMaster> findByProductName(String productName) {
        return productMasterRepository.findByProductName(productName);
    }
}
