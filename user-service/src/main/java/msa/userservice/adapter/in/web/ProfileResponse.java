package msa.userservice.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String loginId;
    private String name;
    private String email;
}
