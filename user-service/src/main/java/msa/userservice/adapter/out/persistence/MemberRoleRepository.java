package msa.userservice.adapter.out.persistence;

import msa.userservice.domain.Member;
import msa.userservice.domain.MemberRole;
import msa.userservice.domain.MemberRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRole, MemberRoleId> {
    List<MemberRole> findByMember(Member member);
}
