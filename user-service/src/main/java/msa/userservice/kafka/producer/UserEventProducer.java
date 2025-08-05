package msa.userservice.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import msa.userservice.domain.Member;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {
    private static final String TOPIC = "user-creation-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMemberCreatedEvent(Member member) {
        ObjectNode event = objectMapper.createObjectNode();
        event.put("type", "member_created");
        event.put("memberId", member.getMemberId());
        event.put("loginId", member.getLoginId());
        event.put("name", member.getName());
        event.put("email", member.getEmail());
        kafkaTemplate.send(TOPIC, event);
    }
}
