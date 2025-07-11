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

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBizNo() {
        return bizNo;
    }
}