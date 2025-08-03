package msa.userservice.adapter.in.web.controller;

import msa.userservice.adapter.in.web.dto.LoginRequest;
import msa.userservice.adapter.in.web.dto.LoginResponse;
import msa.userservice.application.port.in.LoginUseCase;
import msa.userservice.config.MDCHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        MDCHelper.appendDebug(LoginController.class, "로그인 요청 수신. Username: " + loginRequest.getUsername());
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        
        return ResponseEntity.ok(loginResponse);
    }
}