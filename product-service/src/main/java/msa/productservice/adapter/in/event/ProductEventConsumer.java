package msa.productservice.adapter.in.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.productservice.application.port.in.ProductIndexCommandUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;           // ✅ kafka RetryableTopic
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;             // ✅ retry Backoff (중요)
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final ProductIndexCommandUseCase productSearchUseCase;

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(
                    delay = 2000L,
                    multiplier = 2.0,
                    maxDelay = 60000L
            ),
            autoCreateTopics = "true",
            include = { Exception.class },
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
            topics = "product-events",
            groupId = "product-service-group",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "3"
    )
    @Transactional
    public void consume(JsonNode message, Acknowledgment ack) {
        long productCode = message.path("productCode").asLong();
        long version     = message.path("version").asLong(0L);

        try {
            productSearchUseCase.reindexByProductCode(productCode, version);
            ack.acknowledge();
            log.info("[Kafka] consume OK - productCode={}, version={}", productCode, version);
        } catch (Exception e) {
            log.error("[Kafka] consume 실패 - productCode={}, version={} → 재시도/또는 DLT", productCode, version, e);
            throw e; // 재시도/DTL로 라우팅되도록 다시 던짐
        }
    }
}
