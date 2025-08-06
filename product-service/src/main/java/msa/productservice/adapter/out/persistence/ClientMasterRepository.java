package msa.productservice.adapter.out.persistence;

import msa.productservice.domain.ClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientMasterRepository extends JpaRepository<ClientMaster, Integer> {
}
