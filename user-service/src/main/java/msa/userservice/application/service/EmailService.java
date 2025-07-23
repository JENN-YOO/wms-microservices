package msa.userservice.application.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 8자리 인증코드 생성
    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            int idx = rnd.nextInt(3);
            switch (idx) {
                case 0: key.append((char) (rnd.nextInt(26) + 97)); break; // a-z
                case 1: key.append((char) (rnd.nextInt(26) + 65)); break; // A-Z
                case 2: key.append(rnd.nextInt(10)); break; // 0-9
            }
        }
        return key.toString();
    }

    private MimeMessage createMessage(String to, String code) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("[회원가입] 이메일 인증코드 안내");
        String html = "<div>아래 인증코드를 입력하세요.<br><b>" + code + "</b></div>";
        message.setText(html, "utf-8", "html");
        message.setFrom(new InternetAddress(fromEmail, "서비스명"));
        return message;
    }

    // 인증코드 발송 & Redis 저장
    public void sendLoginAuthMessage(String to) throws Exception {
        String code = createKey();
        MimeMessage message = createMessage(to, code);
        try {
            mailSender.send(message);
            logger.info("메일 전송 성공: {}, 인증코드: {}", to, code);
        } catch (MailException es) {
            logger.error("메일 전송 실패: {} - {}", to, es.getMessage(), es);
            throw new IllegalArgumentException("메일 발송 실패: " + es.getMessage());
        }
        redisTemplate.opsForValue().set(to, code, Duration.ofMinutes(30));
        logger.info("인증코드 Redis 저장 완료: key={}, code={}", to, code);
    }

    // 코드 검증
    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return savedCode != null && savedCode.equals(code);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public String getCodeFromRedis(String email) {
        return redisTemplate.opsForValue().get(email);
    }

}
