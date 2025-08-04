package msa.userservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.adapter.in.web.dto.ClientUpdateRequest;
import msa.userservice.adapter.in.web.dto.ClientUpdateResponse;
import msa.userservice.adapter.in.web.dto.UpdateProfileRequest;
import msa.userservice.adapter.out.persistence.ClientMasterRepository;
import msa.userservice.application.port.in.ClientUpdateUseCase;
import msa.userservice.domain.ClientMaster;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientUpdateService implements ClientUpdateUseCase {

    private final ClientMasterRepository clientMasterRepository;


    @Override
    public ClientUpdateResponse update(int clientCode, ClientUpdateRequest clientUpdateRequest) {
        ClientMaster client = clientMasterRepository.findByClientCode(clientCode)
                .orElse(null);

        if(client == null) {
            return new ClientUpdateResponse(false, "화주 코드를 찾을 수 없습니다.");
        }

        if(clientUpdateRequest.getClientName() != null) client.setClientName(clientUpdateRequest.getClientName());
        if(clientUpdateRequest.getClientBusinessNumber() != null) client.setClientBusinessNumber(clientUpdateRequest.getClientBusinessNumber());
        if(clientUpdateRequest.getBusinessCode() != null) client.setBusinessCode(clientUpdateRequest.getBusinessCode());
        if(clientUpdateRequest.getCeoName() != null) client.setCeoName(clientUpdateRequest.getCeoName());
        if(clientUpdateRequest.getAddressBase() != null) client.setAddressBase(clientUpdateRequest.getAddressBase());
        if(clientUpdateRequest.getAddressDetail() != null) client.setAddressDetail(clientUpdateRequest.getAddressDetail());
        if(clientUpdateRequest.getZipcode() != null) client.setZipcode(clientUpdateRequest.getZipcode());
        if(clientUpdateRequest.getManagerName() != null) client.setManagerName(clientUpdateRequest.getManagerName());
        if(clientUpdateRequest.getManagerMobileEncrypted() != null) client.setManagerMobileEncrypted(clientUpdateRequest.getManagerMobileEncrypted());
        if(clientUpdateRequest.getManagerEmailEncrypted() != null) client.setManagerEmailEncrypted(clientUpdateRequest.getManagerEmailEncrypted());
        if(clientUpdateRequest.getFaxNumber() != null) client.setFaxNumber(clientUpdateRequest.getFaxNumber());
        if(clientUpdateRequest.getRemarks() != null) client.setRemarks(clientUpdateRequest.getRemarks());

        return new ClientUpdateResponse(true, "화주 정보가 수정되었습니다.");
    }

}
