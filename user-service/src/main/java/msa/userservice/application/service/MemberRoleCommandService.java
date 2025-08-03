package msa.userservice.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.MemberRoleRequest;
import msa.userservice.adapter.in.web.dto.MemberRoleResponse;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.adapter.out.persistence.RoleRepository;
import msa.userservice.application.port.in.MemberRoleCommandUseCase;
import msa.userservice.application.port.out.MemberRolePersistencePort;
import msa.userservice.domain.Member;
import msa.userservice.domain.MemberRole;
import msa.userservice.domain.MemberRoleId;
import msa.userservice.domain.Role;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberRoleCommandService implements MemberRoleCommandUseCase {

    private final MemberRolePersistencePort memberRolePersistencePort;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public MemberRoleResponse assignRole(MemberRoleRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않습니다."));

        MemberRoleId memberRoleId = new MemberRoleId(member.getMemberId(), role.getId());
        MemberRole memberRole = new MemberRole();
        memberRole.setId(memberRoleId);
        memberRole.setMember(member);
        memberRole.setRole(role);

        memberRolePersistencePort.save(memberRole);

        return new MemberRoleResponse(true, "역할이 성공적으로 부여되었습니다.");
    }
}
