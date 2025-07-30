package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.RoleRequest;
import msa.userservice.adapter.in.web.dto.RoleResponse;
import msa.userservice.application.port.in.RoleCommandUseCase;
import msa.userservice.application.port.out.RolePersistencePort;
import msa.userservice.domain.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleCommandService implements RoleCommandUseCase {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public RoleResponse save(RoleRequest request) {

        Role role = new Role();
        role.setRoleName(request.getRoleName());

        rolePersistencePort.save(role);

        return new RoleResponse(true, "역할이 성공적으로 저장되었습니다.");
    }
}

