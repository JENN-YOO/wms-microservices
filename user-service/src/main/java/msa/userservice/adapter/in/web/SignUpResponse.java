package msa.userservice.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponse {
    private boolean success;
    private String message;

    public SignUpResponse() {
    }

    public SignUpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}