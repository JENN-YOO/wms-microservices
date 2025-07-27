package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.ProfileResponse;

public interface ProfileQueryUseCase {
    ProfileResponse getProfile(String loginId);
}
