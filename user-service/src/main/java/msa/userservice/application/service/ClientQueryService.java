package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.out.persistence.ClientMasterRepository;
import msa.userservice.application.port.in.ClientQueryUseCase;
import msa.userservice.domain.ClientMaster;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientQueryService implements ClientQueryUseCase {

    private final ClientMasterRepository clientMasterRepository;

    @Override
    @Transactional(readOnly = true)
    public String getClientName(int clientCode) {
        return clientMasterRepository.findById(clientCode)
                .map(ClientMaster::getClientName)
                .orElse(null);
    }
}
