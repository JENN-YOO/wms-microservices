package msa.productservice.adapter.in.event;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.productservice.application.port.in.ProductIndexCommandUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final ProductIndexCommandUseCase productSearchUseCase;

    @KafkaListener(
            topics = "product-events",
            groupId = "product-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(JsonNode message, Acknowledgment ack) {
        try {
            String type = message.path("type").asText(); // product_created | product_updated
            long productCode = message.path("productCode").asLong();

            // 인덱스 갱신
            productSearchUseCase.reindexByProductCode(productCode);

            ack.acknowledge();
        } catch (Exception e) {
            log.error("ProductEvent consume 실패", e);
            // ack 안 함 → 재시도
        }
    }
}
