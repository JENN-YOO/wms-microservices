package msa.userservice.application.service;

import msa.userservice.application.port.in.SignUpUseCase;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignUpService implements SignUpUseCase {

    private final MemberPersistencePort memberPersistencePort;

    public SignUpService(MemberPersistencePort memberPersistencePort) {
        this.memberPersistencePort = memberPersistencePort;
    }

    @Override
    public Member signUp(Member member) {
        // Here you can add business logic, e.g., password encoding, validation
        return memberPersistencePort.save(member);
    }
}
