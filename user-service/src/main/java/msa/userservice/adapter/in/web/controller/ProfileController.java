package msa.userservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.ProfileResponse;
import msa.userservice.adapter.in.web.dto.UpdateProfileRequest;
import msa.userservice.adapter.in.web.dto.UpdateProfileResponse;
import msa.userservice.application.port.in.ProfileCommandUseCase;
import msa.userservice.application.port.in.ProfileQueryUseCase;
import msa.userservice.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileQueryUseCase profileUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfileCommandUseCase profileCommandUseCase;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String loginId = jwtTokenProvider.getLoginId(token);

        // 추가: roles 추출
        String rolesString = jwtTokenProvider.getRoles(token); // "ADMIN,USER" 등
        List<String> roles = Arrays.asList(rolesString.split(",")); // List<String> 변환


        ProfileResponse profileResponse = profileUseCase.getProfile(loginId, roles);
        return ResponseEntity.ok(profileResponse);
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateProfileRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");
        String loginId = jwtTokenProvider.getLoginId(token);
        UpdateProfileResponse response = profileCommandUseCase.updateProfile(loginId, request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
