package msa.userservice.application.port.out;

import msa.userservice.domain.Role;

public interface RolePersistencePort {
    Role save(Role role);
}
