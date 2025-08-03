package msa.userservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.RegisterRequest;
import msa.userservice.adapter.in.web.dto.RegisterResponse;
import msa.userservice.application.port.in.ClientRegisterUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientRegisterUseCase clientRegisterUseCase;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = clientRegisterUseCase.register(registerRequest);

        if(registerResponse.isSuccess()){
          return ResponseEntity.ok(new RegisterResponse(true, "화주등록 성공!"));
        } else {
            logger.warn("화주등록 실패 : {}", registerResponse.getMessage());
        }
        return ResponseEntity.badRequest().body(registerResponse);
    }
}
