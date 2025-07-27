package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.UpdateProfileRequest;
import msa.userservice.adapter.in.web.UpdateProfileResponse;
import msa.userservice.application.port.in.ProfileCommandUseCase;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandService implements ProfileCommandUseCase {
    private final MemberPersistencePort memberPersistencePort;

    @Override
    public UpdateProfileResponse updateProfile(String loginId, UpdateProfileRequest req) {
        Member member = memberPersistencePort.findByLoginId(loginId)
                .orElse(null);
        if (member == null) {
            return new UpdateProfileResponse(false, "회원 정보를 찾을 수 없습니다.");
        }
        if (req.getName() != null) member.setName(req.getName());
        if (req.getPhoneNo() != null) member.setPhoneNo(req.getPhoneNo());
        if (req.getCompanyName() != null) member.setCompanyName(req.getCompanyName());

        return new UpdateProfileResponse(true, "회원정보가 수정되었습니다.");
    }
}
