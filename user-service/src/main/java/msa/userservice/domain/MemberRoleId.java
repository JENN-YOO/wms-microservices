package msa.userservice.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MemberRoleId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "role_id")
    private Long roleId;

    // Constructors, equals, hashCode

    public MemberRoleId() {}

    public MemberRoleId(Long memberId, Long roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberRoleId that = (MemberRoleId) o;
        return Objects.equals(memberId, that.memberId) &&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roleId);
    }
}
