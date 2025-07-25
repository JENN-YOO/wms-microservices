package msa.userservice.adapter.in.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String clientCode;
    private String businessCode;
    private String clientName;
    private String clientBusinessNumber;
    private String mainPhoneEncrypted;
    private String faxNumber;
    private String ceoName;
    private String managerName;
    private String managerMobileEncrypted;
    private String managerEmailEncrypted;
    private String zipcode;
    private String addressBase;
    private String addressDetail;
    private String remarks;
}
