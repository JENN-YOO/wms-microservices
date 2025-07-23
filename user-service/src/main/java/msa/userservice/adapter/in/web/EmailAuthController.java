package msa.userservice.adapter.in.web;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.application.port.in.EmailAuthUseCase;
import msa.userservice.application.service.EmailService;
import msa.userservice.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-auth")
@RequiredArgsConstructor
public class EmailAuthController {
    private static final Logger logger = LoggerFactory.getLogger(EmailAuthController.class);

    private final EmailAuthUseCase emailAuthUseCase;
    private final MemberRepository memberRepository;
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendAuthEmail(@RequestBody EmailSendRequest req) {
        try {
            emailAuthUseCase.sendAuthEmail(req.getEmail());
            logger.info("인증 메일 발송 성공: {}", req.getEmail());
            return ResponseEntity.ok("인증 메일이 발송되었습니다.");
        } catch (Exception e) {
            logger.error("인증 메일 발송 실패: {} - {}", req.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body("메일 발송 실패");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailAuthRequest dto) {
        String redisCode = emailService.getCodeFromRedis(dto.getEmail());
        logger.info("이메일 인증 시도: email={}, 입력 코드={}, Redis 저장 코드={}", dto.getEmail(), dto.getAuthCode(), redisCode);

        boolean valid = emailAuthUseCase.verifyAuthCode(dto.getEmail(), dto.getAuthCode());
        logger.info("이메일 인증 검증 결과: email={}, valid={}", dto.getEmail(), valid);

        if (!valid) {
            logger.warn("이메일 인증 실패: email={}, 입력 코드={}, Redis 저장 코드={}", dto.getEmail(), dto.getAuthCode(), redisCode);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증코드가 올바르지 않거나 만료되었습니다.");
        }
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    logger.error("이메일 인증 성공 직후 회원정보 조회 실패: email={}", dto.getEmail());
                    return new RuntimeException("회원 정보를 찾을 수 없습니다.");
                });
        member.setStatusCode(1);
        memberRepository.save(member);
        logger.info("이메일 인증 성공 및 회원 상태 업데이트: email={}", dto.getEmail());

        return ResponseEntity.ok("이메일 인증 성공! 이제 로그인 가능합니다.");
    }
}
