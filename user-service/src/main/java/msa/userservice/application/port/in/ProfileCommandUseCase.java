package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.UpdateProfileRequest;
import msa.userservice.adapter.in.web.UpdateProfileResponse;

public interface ProfileCommandUseCase {
    UpdateProfileResponse updateProfile(String loginId, UpdateProfileRequest request);
}
