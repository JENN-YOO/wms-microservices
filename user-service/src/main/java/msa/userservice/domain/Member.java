package msa.userservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_member")
@Getter
@Setter
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

    @Column(name = "client_code", length = 10)
    private String clientCode;

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

    @Column(name = "status_code")
    private Integer statusCode = 0;

}
