package msa.productservice.adapter.in.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.out.persistence.ClientMasterRepository;
import msa.productservice.config.MDCHelper;
import msa.productservice.domain.ClientMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
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
            groupId = "product-service-group"
    )
    @Transactional
    public void consume(JsonNode message) {
        MDCHelper.appendDebug(this.getClass(), "Kafka 이벤트 수신: " + message.toString());

        String type = getString(message, "type");
        if (!"client_created".equals(type)) {
            log.warn("Unknown event type received: {}", type);
            MDCHelper.appendDebug(this.getClass(), "알 수 없는 이벤트 타입: " + type);
            return;
        }

        int clientCode = getInt(message, "clientCode");
        String businessCode = getString(message, "businessCode");
        String clientName = getString(message, "clientName");
        String clientBusinessNumber = getString(message, "clientBusinessNumber");
        String serviceStatus = getString(message, "serviceStatus");
        LocalDate serviceStartDate = getLocalDate(message, "serviceStartDate");
        LocalDate serviceEndDate = getLocalDate(message, "serviceEndDate");

        // 이미 존재하면 생성 거부 및 로깅
        if (clientMasterRepository.existsById(clientCode)) {
            log.warn("중복 생성 시도: 이미 존재하는 clientCode 수신 - {}", clientCode);
            MDCHelper.appendDebug(this.getClass(), "중복된 clientCode로 인한 등록 거부: " + clientCode);
            return;
        }

        // 신규 등록
        ClientMaster entity = ClientMaster.builder()
                .clientCode(clientCode)
                .businessCode(businessCode)
                .clientName(clientName)
                .clientBusinessNumber(clientBusinessNumber)
                .serviceStatus(serviceStatus)
                .serviceStartDate(serviceStartDate)
                .serviceEndDate(serviceEndDate)
                .build();

        clientMasterRepository.save(entity);

        log.info("[Kafka] ClientMaster 복제(Insert) 완료: clientCode={}, clientName={}", clientCode, clientName);
        MDCHelper.appendDebug(this.getClass(), "복제(Insert) 완료: clientCode=" + clientCode);
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
