package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.ProfileResponse;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.application.port.in.ProfileQueryUseCase;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileQueryService implements ProfileQueryUseCase {

    private final MemberPersistencePort memberPersistencePort;

    @Override
    public ProfileResponse getProfile(String loginId) {
        Member member = memberPersistencePort.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new ProfileResponse(member.getLoginId(), member.getName(), member.getEmail());
    }
}
