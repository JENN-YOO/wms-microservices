package msa.userservice.adapter.in.web;

import msa.userservice.application.port.in.GetProfileUseCase;
import msa.userservice.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final GetProfileUseCase getProfileUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public ProfileController(GetProfileUseCase getProfileUseCase, JwtTokenProvider jwtTokenProvider) {
        this.getProfileUseCase = getProfileUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String loginId = jwtTokenProvider.getLoginId(token);
        ProfileResponse profileResponse = getProfileUseCase.getProfile(loginId);
        return ResponseEntity.ok(profileResponse);
    }
}
