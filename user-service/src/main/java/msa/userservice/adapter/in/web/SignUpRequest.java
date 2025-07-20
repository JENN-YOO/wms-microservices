package msa.userservice.adapter.in.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phoneNo;
    private String companyName;
    private String bizNo;
}