package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.RegisterRequest;
import msa.userservice.adapter.in.web.dto.RegisterResponse;
import msa.userservice.adapter.out.event.dto.ClientMasterEvent;
import msa.userservice.adapter.out.persistence.ClientMasterRepository;
import msa.userservice.application.port.in.ClientRegisterUseCase;
import msa.userservice.application.port.out.ClientEventPublisherPort;
import msa.userservice.config.MDCHelper;
import msa.userservice.domain.ClientMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ClientRegisterService implements ClientRegisterUseCase {

    private static final Logger log = LoggerFactory.getLogger(ClientRegisterService.class);

    private final ClientMasterRepository clientMasterRepository;
    private final ClientEventPublisherPort clientEventPublisherPort;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        MDCHelper.appendDebug(ClientRegisterService.class, "화주 등록 요청 시작");

        ClientMaster clientMaster = ClientMaster.builder()
                .businessCode(registerRequest.getBusinessCode())
                .clientName(registerRequest.getClientName())
                .clientBusinessNumber(registerRequest.getClientBusinessNumber())
                .mainPhoneEncrypted(registerRequest.getMainPhoneEncrypted())
                .faxNumber(registerRequest.getFaxNumber())
                .ceoName(registerRequest.getCeoName())
                .managerName(registerRequest.getManagerName())
                .managerEmailEncrypted(registerRequest.getManagerEmailEncrypted())
                .managerMobileEncrypted(registerRequest.getManagerMobileEncrypted())
                .zipcode(registerRequest.getZipcode())
                .addressBase(registerRequest.getAddressBase())
                .addressDetail(registerRequest.getAddressDetail())
                .remarks(registerRequest.getRemarks())
                .build();

        MDCHelper.appendDebug(ClientRegisterService.class,
                "ClientMaster 엔티티 생성 완료");

        clientMasterRepository.save(clientMaster);

        MDCHelper.appendDebug(ClientRegisterService.class,
                "DB 저장 완료. clientCode=" + clientMaster.getClientCode());

        ClientMasterEvent event = toEventDto(clientMaster);

        MDCHelper.appendDebug(ClientRegisterService.class,
                "이벤트 DTO 생성 완료: clientCode=" + event.getClientCode());

        try {
            clientEventPublisherPort.publishClientCreatedEvent(event);
            MDCHelper.appendDebug(ClientRegisterService.class, "이벤트 발행 성공");
        } catch (Exception e) {
            log.error("ClientCreatedEvent publish failed. clientCode={}, error={}",
                    clientMaster.getClientCode(), e.toString());
            MDCHelper.appendDebug(ClientRegisterService.class, "ClientCreatedEvent 발행 실패: " + e.getMessage());
        }

        return new RegisterResponse(true, "화주등록 성공!");
    }

    private ClientMasterEvent toEventDto(ClientMaster clientMaster) {
        return ClientMasterEvent.builder()
                .clientCode(clientMaster.getClientCode())
                .clientName(clientMaster.getClientName())
                .serviceStatus(clientMaster.getServiceStatus())
                .serviceStartDate(clientMaster.getServiceStartDate())
                .serviceEndDate(clientMaster.getServiceEndDate())
                .build();
    }
}
