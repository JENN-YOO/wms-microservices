package msa.userservice.application.port.out;

import msa.userservice.adapter.out.event.dto.ClientMasterEvent;

public interface ClientEventPublisherPort {
    void publishClientCreatedEvent(ClientMasterEvent event);
}
