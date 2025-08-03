package msa.userservice.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import msa.userservice.application.port.out.MemberRolePersistencePort;
import msa.userservice.domain.MemberRole;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberRolePersistenceAdapter implements MemberRolePersistencePort {
    private final MemberRoleRepository memberRoleRepository;

    @Override
    public MemberRole save(MemberRole memberRole) {
        return memberRoleRepository.save(memberRole);
    }
}
