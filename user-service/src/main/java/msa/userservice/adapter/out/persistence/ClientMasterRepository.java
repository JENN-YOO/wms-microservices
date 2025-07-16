package msa.userservice.adapter.out.persistence;

import msa.userservice.domain.ClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientMasterRepository extends JpaRepository<ClientMaster, String> {
}
