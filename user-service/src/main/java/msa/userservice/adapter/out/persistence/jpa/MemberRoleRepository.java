package msa.userservice.adapter.out.persistence.jpa;

import msa.userservice.domain.MemberRole;
import msa.userservice.domain.MemberRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, MemberRoleId> {
}
