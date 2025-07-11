package msa.userservice.application.port.out;

import msa.userservice.domain.Member;

public interface MemberPersistencePort {
    Member save(Member member);
}