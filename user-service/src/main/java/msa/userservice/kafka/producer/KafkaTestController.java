package msa.userservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.out.persistence.MemberRepository;
import msa.userservice.application.port.out.MemberPersistencePort;
import msa.userservice.domain.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka-test")
@RequiredArgsConstructor
public class KafkaTestController {
    private final UserEventProducer userEventProducer;
    private final MemberPersistencePort memberPersistencePort;

    @PostMapping("/user/{loginId}")
    public String sendUserCreationEvent(@PathVariable String loginId) {
        Member member = memberPersistencePort.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        userEventProducer.sendMemberCreatedEvent(member);
        return "Member event sent for loginId: " + loginId;
    }
}
