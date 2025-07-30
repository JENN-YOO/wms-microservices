package msa.userservice.application.port.out;

import msa.userservice.domain.MemberRole;

public interface MemberRolePersistencePort {
    MemberRole save(MemberRole memberRole);
}
