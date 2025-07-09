package msa.userservice.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_type", nullable = false, length = 20)
    private String memberType;

    @Column(name = "login_id", unique = true, nullable = false, length = 100)
    private String loginId;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "biz_no", length = 20)
    private String bizNo;

    @Column(name = "use_yn", length = 1)
    private char useYn = 'y';

    @Column(name = "last_login_dt")
    private LocalDateTime lastLoginDt;

    @Column(name = "reg_dt", updatable = false)
    private LocalDateTime regDt = LocalDateTime.now();

    @Column(name = "mod_dt")
    private LocalDateTime modDt = LocalDateTime.now();

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public char getUseYn() {
        return useYn;
    }

    public void setUseYn(char useYn) {
        this.useYn = useYn;
    }

    public LocalDateTime getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(LocalDateTime lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public LocalDateTime getRegDt() {
        return regDt;
    }

    public void setRegDt(LocalDateTime regDt) {
        this.regDt = regDt;
    }

    public LocalDateTime getModDt() {
        return modDt;
    }

    public void setModDt(LocalDateTime modDt) {
        this.modDt = modDt;
    }
}
