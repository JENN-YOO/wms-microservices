package msa.userservice.adapter.in.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequest {
    private String email;
    private String authCode;
}
