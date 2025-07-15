package msa.userservice.application.service;

import msa.userservice.adapter.in.web.ProfileResponse;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.application.port.in.GetProfileUseCase;
import msa.userservice.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetProfileService implements GetProfileUseCase {

    private final MemberRepository memberRepository;

    public GetProfileService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public ProfileResponse getProfile(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new ProfileResponse(member.getLoginId(), member.getName(), member.getEmail());
    }
}
