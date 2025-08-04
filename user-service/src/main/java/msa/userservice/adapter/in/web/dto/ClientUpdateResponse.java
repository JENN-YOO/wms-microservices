package msa.userservice.adapter.in.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUpdateResponse {
    private boolean success;
    private String message;

    public ClientUpdateResponse() {
    }

    public ClientUpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isClientUpdateSuccess() {
        return success;
    }
}
