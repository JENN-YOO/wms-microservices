package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.MemberRoleRequest;
import msa.userservice.adapter.in.web.dto.MemberRoleResponse;

public interface MemberRoleCommandUseCase {
    MemberRoleResponse assignRole(MemberRoleRequest request);
}

