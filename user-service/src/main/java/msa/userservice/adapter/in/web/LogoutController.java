package msa.userservice.adapter.in.web;

import lombok.RequiredArgsConstructor;
import msa.userservice.application.port.in.LogoutUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final LogoutUseCase logoutUseCase;

    public LogoutController(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken, @RequestHeader("username") String username) {
        logoutUseCase.logout(accessToken.replace("Bearer ", ""), username);
        return ResponseEntity.ok().build();
    }
}