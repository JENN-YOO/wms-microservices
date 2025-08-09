package msa.productservice.adapter.in.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.out.persistence.ClientMasterRepository;
import msa.productservice.config.MDCHelper;
import msa.productservice.domain.ClientMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ClientMasterEventConsumer {

    private final ClientMasterRepository clientMasterRepository;
    private static final Logger log = LoggerFactory.getLogger(ClientMasterEventConsumer.class);

    @KafkaListener(
            topics = "client-created-topic",
            groupId = "product-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(JsonNode message, Acknowledgment ack) {
        try {
            String type = getString(message, "type");
            if (!"client_created".equals(type)) {
                log.warn("Unknown event type received: {}", type);
                ack.acknowledge(); // 잘못된 이벤트도 소비 완료 처리
                return;
            }

            int clientCode = getInt(message, "clientCode");

            // 멱등성: 이미 있으면 커밋 후 종료
            if (clientMasterRepository.existsById((long) clientCode)) {
                log.warn("중복 생성 시도: 이미 존재하는 clientCode - {}", clientCode);
                ack.acknowledge();
                return;
            }

            // 신규 등록
            ClientMaster entity = ClientMaster.builder()
                    .clientCode(clientCode)
                    .businessCode(getString(message, "businessCode"))
                    .clientName(getString(message, "clientName"))
                    .clientBusinessNumber(getString(message, "clientBusinessNumber"))
                    .serviceStatus(getString(message, "serviceStatus"))
                    .serviceStartDate(getLocalDate(message, "serviceStartDate"))
                    .serviceEndDate(getLocalDate(message, "serviceEndDate"))
                    .build();

            clientMasterRepository.save(entity);

            log.info("[Kafka] ClientMaster 복제(Insert) 완료: clientCode={}, clientName={}", clientCode, entity.getClientName());
            ack.acknowledge(); // **정상처리 시에만 커밋**
        } catch (Exception e) {
            log.error("Kafka consume 실패", e);
            // 커밋X → Kafka가 재시도하게 놔둠(재처리)
        }
    }

    private String getString(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

    private int getInt(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asInt() : 0;
    }

    private LocalDate getLocalDate(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? LocalDate.parse(node.get(field).asText()) : null;
    }
}
