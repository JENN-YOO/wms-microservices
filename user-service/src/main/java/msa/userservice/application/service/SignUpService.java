package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.SignUpRequest;
import msa.userservice.adapter.in.web.dto.SignUpResponse;
import msa.userservice.application.port.in.SignUpUseCase;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (memberPersistencePort.findByLoginId(signUpRequest.getLoginId()).isPresent()) {
            return new SignUpResponse(false, "이미 존재하는 아이디입니다.");
        }

        Member member = new Member();
        member.setLoginId(signUpRequest.getLoginId());
        member.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        member.setName(signUpRequest.getName());
        member.setEmail(signUpRequest.getEmail());
        member.setPhoneNo(signUpRequest.getPhoneNo());
        member.setCompanyName(signUpRequest.getCompanyName());
        member.setClientCode(signUpRequest.getClientCode());
        member.setBizNo(signUpRequest.getBizNo());
        member.setMemberType("USER"); // 기본값 설정
        member.setStatusCode(0);

        memberPersistencePort.save(member);

        return new SignUpResponse(true, "회원가입 성공! 이메일 인증을 완료해 주세요.");
    }
}