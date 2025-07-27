package msa.userservice.application.port.out;

import msa.userservice.domain.Member;

import java.util.Optional;

public interface MemberPersistencePort {
    Member save(Member member);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByEmail(String email);
}