package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.ProfileResponse;
import msa.userservice.adapter.out.persistence.MemberRoleRepository;
import msa.userservice.application.port.in.ProfileQueryUseCase;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import msa.userservice.domain.MemberRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileQueryService implements ProfileQueryUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberRoleRepository memberRoleRepository;

    @Override
    public ProfileResponse getProfile(String loginId, List<String> roles) {
        Member member = memberPersistencePort.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

//        List<MemberRole> memberRoles = memberRoleRepository.findByMember(member);
//        List<String> roleNames = memberRoles.stream() // 리스트를 스트림(흐름)으로 변환
//                .map(mr -> mr.getRole().getRoleName())    // 각각의 mr에서 Role을 꺼내고, RoleName만 추출
//                .collect(Collectors.toList());            // 결과를 다시 리스트로 모음

//        List<String> roleNames = new ArrayList<>();
//        for (MemberRole mr : memberRoles) {
//            Role role = mr.getRole();
//            String roleName = role.getRoleName();
//            roleNames.add(roleName);
//        }


        return new ProfileResponse(
                member.getLoginId(),
                member.getName(),
                member.getEmail(),
                roles    // JWT에서 뽑은 roles 바로 사용
        );
    }
}
