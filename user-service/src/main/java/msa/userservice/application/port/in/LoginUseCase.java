package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.LoginRequest;
import msa.userservice.adapter.in.web.dto.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
