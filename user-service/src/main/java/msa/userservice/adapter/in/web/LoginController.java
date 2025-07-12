package msa.userservice.adapter.in.web;

import lombok.RequiredArgsConstructor;
import msa.userservice.application.port.in.LoginUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for username: {}", loginRequest.getUsername());
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        
        return ResponseEntity.ok(loginResponse);
    }
}