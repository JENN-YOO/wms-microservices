package msa.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "tb_client_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientCode;

    @Column(name = "business_code", length = 24)
    private String businessCode;

    @Column(name = "client_name", length = 256)
    private String clientName;

    @Column(name = "client_business_number", length = 20)
    private String clientBusinessNumber;

    @Column(name = "main_phone_encrypted", length = 512)
    private String mainPhoneEncrypted;

    @Column(name = "fax_number", length = 100)
    private String faxNumber;

    @Column(name = "ceo_name", length = 256)
    private String ceoName;

    @Column(name = "manager_name", length = 256)
    private String managerName;

    @Column(name = "manager_mobile_encrypted", length = 512)
    private String managerMobileEncrypted;

    @Column(name = "manager_email_encrypted", length = 512)
    private String managerEmailEncrypted;

    @Column(name = "zipcode", length = 7)
    private String zipcode;

    @Column(name = "address_base", length = 256)
    private String addressBase;

    @Column(name = "address_detail", length = 256)
    private String addressDetail;

    @Column(name = "remarks", length = 4000)
    private String remarks;

    @Column(name = "service_status", length = 2)
    private String serviceStatus;

    @Column(name = "service_start_date")
    private LocalDate serviceStartDate;

    @Column(name = "service_end_date")
    private LocalDate serviceEndDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 48)
    private String createdBy;


}
