package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.RegisterRequest;
import msa.userservice.adapter.in.web.dto.RegisterResponse;

public interface ClientRegisterUseCase {
    RegisterResponse register(RegisterRequest registerRequest);
}
