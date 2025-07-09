package msa.userservice.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_client_master")
public class ClientMaster {

    @Id
    @Column(name = "client_code", length = 24)
    private String clientCode;

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

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientBusinessNumber() {
        return clientBusinessNumber;
    }

    public void setClientBusinessNumber(String clientBusinessNumber) {
        this.clientBusinessNumber = clientBusinessNumber;
    }

    public String getMainPhoneEncrypted() {
        return mainPhoneEncrypted;
    }

    public void setMainPhoneEncrypted(String mainPhoneEncrypted) {
        this.mainPhoneEncrypted = mainPhoneEncrypted;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerMobileEncrypted() {
        return managerMobileEncrypted;
    }

    public void setManagerMobileEncrypted(String managerMobileEncrypted) {
        this.managerMobileEncrypted = managerMobileEncrypted;
    }

    public String getManagerEmailEncrypted() {
        return managerEmailEncrypted;
    }

    public void setManagerEmailEncrypted(String managerEmailEncrypted) {
        this.managerEmailEncrypted = managerEmailEncrypted;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddressBase() {
        return addressBase;
    }

    public void setAddressBase(String addressBase) {
        this.addressBase = addressBase;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public LocalDate getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDate serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDate getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDate serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
