package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.RegisterRequest;
import msa.userservice.adapter.in.web.RegisterResponse;

public interface ClientRegisterUseCase {
    RegisterResponse register(RegisterRequest registerRequest);
}
