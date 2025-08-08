package msa.userservice.adapter.out.persistence;

import msa.userservice.domain.ClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientMasterRepository extends JpaRepository<ClientMaster, Integer> {
    Optional<ClientMaster> findByClientCode(int clientCode);
}
