package msa.userservice.adapter.in.web;

import lombok.RequiredArgsConstructor;
import msa.userservice.application.port.in.EmailAuthUseCase;
import msa.userservice.application.port.in.SignUpUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    private final SignUpUseCase signUpUseCase;
    private final EmailAuthUseCase emailAuthUseCase;

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = signUpUseCase.signUp(signUpRequest);
        logger.info("회원가입 요청: {}", signUpRequest.getEmail());

        if (signUpResponse.isSuccess()) {
            try {
                emailAuthUseCase.sendAuthEmail(signUpRequest.getEmail());
                logger.info("회원가입 후 인증 메일 발송 성공: {}", signUpRequest.getEmail());
            } catch (Exception e) {
                logger.error("회원가입 후 인증 메일 발송 실패: {} - {}", signUpRequest.getEmail(), e.getMessage(), e);
                return ResponseEntity.status(500)
                        .body(new SignUpResponse(false, "회원가입은 성공했지만, 인증 메일 발송에 실패했습니다."));
            }
            return ResponseEntity.ok(new SignUpResponse(true, "회원가입 성공! 이메일 인증을 완료해주세요."));
        }
        logger.warn("회원가입 실패: {}", signUpResponse.getMessage());
        return ResponseEntity.badRequest().body(signUpResponse);
    }
}