package msa.productservice.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.productservice.application.port.out.ProductEventPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProductEventAdapter implements ProductEventPort {

    private static final String TOPIC = "product-events";

    private final KafkaTemplate<String, Object> kafkaTemplate; // yml에서 JsonSerializer 설정되어 있음
    private final ObjectMapper objectMapper;

    @Override
    public void publishProductCreated(long productCode, Instant occurredAt, long version) {
        ObjectNode event = objectMapper.createObjectNode();
        event.put("type", "product_created");
        event.put("productCode", productCode);
        event.put("occurredAt", occurredAt.toString());
        event.put("version", version);

        String key = String.valueOf(productCode); // 파티션/순서 보장
        kafkaTemplate.send(TOPIC, key, event);
        log.info("[Kafka] product_created 이벤트 발행: topic={}, key={}, productCode={}", TOPIC, key, productCode);
    }
}
