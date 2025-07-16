package msa.userservice.application.service;

import msa.userservice.adapter.in.web.SignUpRequest;
import msa.userservice.adapter.in.web.SignUpResponse;
import msa.userservice.adapter.out.persistence.jpa.MemberRepository;
import msa.userservice.adapter.out.persistence.mybatis.MemberMapper;
import msa.userservice.application.port.in.SignUpUseCase;
import msa.userservice.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class SignUpService implements SignUpUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public SignUpService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberMapper = memberMapper;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (memberRepository.findByLoginId(signUpRequest.getLoginId()).isPresent()) {
            return new SignUpResponse(false, "이미 존재하는 아이디입니다.");
        }

        Member member = new Member();
        member.setLoginId(signUpRequest.getLoginId());
        member.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        member.setName(signUpRequest.getName());
        member.setEmail(signUpRequest.getEmail());
        member.setPhoneNo(signUpRequest.getPhoneNo());
        member.setCompanyName(signUpRequest.getCompanyName());
        member.setBizNo(signUpRequest.getBizNo());
        member.setMemberType("USER"); // 기본값 설정

        memberRepository.save(member);

        return new SignUpResponse(true, "회원가입 성공");
    }

    public SignUpResponse signUpWithMapper(SignUpRequest signUpRequest) {
        if (memberMapper.findByLoginIdMapper(signUpRequest.getLoginId()).isPresent()) {
            return new SignUpResponse(false, "이미 존재하는 아이디입니다.");
        }

        Member member = new Member();
        member.setLoginId(signUpRequest.getLoginId());
        member.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        member.setName(signUpRequest.getName());
        member.setEmail(signUpRequest.getEmail());
        member.setPhoneNo(signUpRequest.getPhoneNo());
        member.setCompanyName(signUpRequest.getCompanyName());
        member.setBizNo(signUpRequest.getBizNo());
        member.setMemberType("USER");
        member.setUseYn('y');
        member.setRegDt(LocalDateTime.now());
        member.setModDt(LocalDateTime.now());

        memberMapper.save(member);

        return new SignUpResponse(true, "회원가입 성공");
    }
}