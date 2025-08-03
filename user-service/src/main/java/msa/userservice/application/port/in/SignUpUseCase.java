package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.SignUpRequest;
import msa.userservice.adapter.in.web.dto.SignUpResponse;

public interface SignUpUseCase {
    SignUpResponse signUp(SignUpRequest signUpRequest);
}
