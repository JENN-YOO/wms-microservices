package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.ProfileResponse;

public interface GetProfileUseCase {
    ProfileResponse getProfile(String loginId);
}
