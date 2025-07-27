package msa.userservice.adapter.in.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String bizNo;
    private String name;
    private String phoneNo;
    private String companyName;
}
