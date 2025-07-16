package msa.userservice.adapter.out.persistence.jpa;

import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberRepository memberRepository;

    public MemberPersistenceAdapter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }
}