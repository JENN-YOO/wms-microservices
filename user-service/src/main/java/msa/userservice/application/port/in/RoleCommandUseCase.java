package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.RoleRequest;
import msa.userservice.adapter.in.web.dto.RoleResponse;

public interface RoleCommandUseCase {
    RoleResponse save(RoleRequest request);
}
