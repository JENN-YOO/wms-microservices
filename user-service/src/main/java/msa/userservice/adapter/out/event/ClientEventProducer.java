package msa.userservice.adapter.out.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.out.event.dto.ClientMasterEvent;
import msa.userservice.application.port.out.ClientEventPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientEventProducer implements ClientEventPublisherPort {
    private static final String TOPIC = "client-created-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper; // 반드시 DI로 받아야 함

    @Override
    public void publishClientCreatedEvent(ClientMasterEvent event) {
        ObjectNode message = objectMapper.createObjectNode();
        message.put("type", "client_created");
        message.put("clientCode", event.getClientCode());
        message.put("clientName", event.getClientName());
        message.put("serviceStatus", event.getServiceStatus());
        message.put("serviceStartDate", event.getServiceStartDate() != null ? event.getServiceStartDate().toString() : null);
        message.put("serviceEndDate", event.getServiceEndDate() != null ? event.getServiceEndDate().toString() : null);

        // 1. Key값(clientCode 등) 지정해서 **동일 client 이벤트는 항상 같은 파티션**에!
        kafkaTemplate.send(TOPIC, String.valueOf(event.getClientCode()), message);
    }
}
