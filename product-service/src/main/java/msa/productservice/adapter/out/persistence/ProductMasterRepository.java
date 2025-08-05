package msa.productservice.adapter.out.persistence;

import msa.productservice.domain.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
    Optional<ProductMaster> findByProductName(String productName);
}
