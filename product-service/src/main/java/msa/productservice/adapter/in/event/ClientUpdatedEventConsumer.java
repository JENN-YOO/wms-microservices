package msa.productservice.adapter.in.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import msa.productservice.application.port.in.ProductSearchUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ClientUpdatedEventConsumer {
    Logger log = LoggerFactory.getLogger(ClientUpdatedEventConsumer.class);
    private final ProductSearchUseCase productSearchUseCase;

    @KafkaListener(
            topics = "client-updated-topic",
            groupId = "product-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(JsonNode message, Acknowledgment ack) {
        try {
            int clientCode = message.path("clientCode").asInt();
            productSearchUseCase.reindexByClientCode((long) clientCode);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("ClientUpdatedEvent consume 실패", e);
        }
    }
}
