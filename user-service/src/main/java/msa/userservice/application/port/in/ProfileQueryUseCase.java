package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.ProfileResponse;

import java.util.List;

public interface ProfileQueryUseCase {
    ProfileResponse getProfile(String loginId, List<String> roles);
}
