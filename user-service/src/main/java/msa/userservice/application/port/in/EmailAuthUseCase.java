package msa.userservice.application.port.in;

public interface EmailAuthUseCase {
    void sendAuthEmail(String email) throws Exception;
    boolean verifyAuthCode(String email, String authCode);
}
