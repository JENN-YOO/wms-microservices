package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.ProfileResponse;

public interface ProfileQueryUseCase {
    ProfileResponse getProfile(String loginId);
}
