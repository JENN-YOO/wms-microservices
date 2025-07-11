package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.LoginRequest;
import msa.userservice.adapter.in.web.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
