package msa.productservice.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MemberEventConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "user-creation-topic", groupId = "member-consumer-group")
    public void consume(JsonNode event) {
        String type;
        if (event.has("type")) {
            type = event.get("type").asText();
        } else {
            type = "member_created";
        }

        if ("member_created".equals(type)) {
            MemberCreatedEvent member = objectMapper.convertValue(event, MemberCreatedEvent.class);
            System.out.println("멤버 이벤트: " + member.getLoginId() + ", " + member.getName());
            System.out.println(member.getEmail());
            System.out.println(member.getMemberId());
        }
        // else if ("product_created".equals(type)) {
        //     ProductCreatedEvent product = objectMapper.convertValue(event, ProductCreatedEvent.class);
        //     System.out.println("상품 이벤트: " + product.getName());
        // }
        else {
            System.out.println("Unknown event type: " + event.toString());
        }
    }
}
