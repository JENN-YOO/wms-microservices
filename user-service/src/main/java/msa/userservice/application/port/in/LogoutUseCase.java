package msa.userservice.application.port.in;

public interface LogoutUseCase {
    void logout(String accessToken, String username);
}
