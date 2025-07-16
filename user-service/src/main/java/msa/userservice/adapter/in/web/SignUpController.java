package msa.userservice.adapter.in.web;

import msa.userservice.application.port.in.SignUpUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final SignUpUseCase signUpUseCase;

    public SignUpController(SignUpUseCase signUpUseCase) {
        this.signUpUseCase = signUpUseCase;
    }

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = signUpUseCase.signUp(signUpRequest);
        if (signUpResponse.isSuccess()) {
            return ResponseEntity.ok(signUpResponse);
        } else {
            return ResponseEntity.badRequest().body(signUpResponse);
        }
    }

    @PostMapping("/mybatis")
    public ResponseEntity<SignUpResponse> signUpWithMyBatis(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = signUpUseCase.signUpWithMapper(signUpRequest);
        if (signUpResponse.isSuccess()) {
            return ResponseEntity.ok(signUpResponse);
        } else {
            return ResponseEntity.badRequest().body(signUpResponse);
        }
    }
}