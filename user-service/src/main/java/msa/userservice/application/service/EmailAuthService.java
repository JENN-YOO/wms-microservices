package msa.userservice.application.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import msa.userservice.application.port.in.EmailAuthUseCase;
import org.springframework.stereotype.Service;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class EmailAuthService implements EmailAuthUseCase {
    private final EmailService emailService;

    @Override
    public void sendAuthEmail(String email) throws Exception {
        emailService.sendLoginAuthMessage(email);
    }

    @Override
    public boolean verifyAuthCode(String email, String authCode) {
        return emailService.verifyCode(email, authCode);
    }
}
