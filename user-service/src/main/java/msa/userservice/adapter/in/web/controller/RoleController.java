package msa.userservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.RoleRequest;
import msa.userservice.adapter.in.web.dto.RoleResponse;
import msa.userservice.application.port.in.RoleCommandUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleCommandUseCase roleCommandUseCase;

    @PostMapping("/create")
    public ResponseEntity<RoleResponse> signUp(@RequestBody RoleRequest roleRequest) {
        RoleResponse response = roleCommandUseCase.save(roleRequest);
        if (roleRequest.getRoleName() == null || roleRequest.getRoleName().isEmpty()) {
            return ResponseEntity.badRequest().body(new RoleResponse(false, "역할 이름이 필요합니다."));
        }
        if (!response.isSuccess()) {
            return ResponseEntity.status(500).body(new RoleResponse(false, "역할 생성 실패: " + response.getMessage()));
        }
        return ResponseEntity.ok(new RoleResponse(true, "역할 생성 완료."));
    }
}
