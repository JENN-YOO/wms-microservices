package msa.userservice.adapter.in.web.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequest {
    private String email;
    private String authCode;
}
