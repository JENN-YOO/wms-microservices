package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.SignUpRequest;
import msa.userservice.adapter.in.web.SignUpResponse;

public interface SignUpUseCase {
    SignUpResponse signUp(SignUpRequest signUpRequest);
}
