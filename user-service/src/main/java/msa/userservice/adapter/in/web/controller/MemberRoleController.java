package msa.userservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.MemberRoleRequest;
import msa.userservice.adapter.in.web.dto.MemberRoleResponse;
import msa.userservice.application.port.in.MemberRoleCommandUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-roles")
@RequiredArgsConstructor
public class MemberRoleController {

    private final MemberRoleCommandUseCase memberRoleCommandUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<MemberRoleResponse> assignRoleToMember(@RequestBody MemberRoleRequest request) {
        if (request.getMemberId() == null || request.getRoleId() == null) {
            return ResponseEntity.badRequest().body(new MemberRoleResponse(false, "memberId, roleId는 필수입니다."));
        }
        try {
            MemberRoleResponse response = memberRoleCommandUseCase.assignRole(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MemberRoleResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MemberRoleResponse(false, "서버 오류: " + e.getMessage()));
        }
    }
}

