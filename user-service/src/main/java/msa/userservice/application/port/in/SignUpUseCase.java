package msa.userservice.application.port.in;

import msa.userservice.domain.Member;

public interface SignUpUseCase {
    Member signUp(Member member);
}
