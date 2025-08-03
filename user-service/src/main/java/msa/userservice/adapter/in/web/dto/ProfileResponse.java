package msa.userservice.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String loginId;
    private String name;
    private String email;
    private List<String> roles;
}
