package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.UpdateProfileRequest;
import msa.userservice.adapter.in.web.dto.UpdateProfileResponse;

public interface ProfileCommandUseCase {
    UpdateProfileResponse updateProfile(String loginId, UpdateProfileRequest request);
}
