package msa.userservice.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import msa.userservice.application.port.out.RolePersistencePort;
import msa.userservice.domain.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter  implements RolePersistencePort {
    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
