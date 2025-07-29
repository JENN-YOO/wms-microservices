package msa.userservice.application.service;

import msa.userservice.adapter.in.web.dto.RegisterRequest;
import msa.userservice.adapter.in.web.dto.RegisterResponse;
import msa.userservice.adapter.out.persistence.ClientMasterRepository;
import msa.userservice.application.port.in.ClientRegisterUseCase;
import msa.userservice.domain.ClientMaster;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClientRegisterService implements ClientRegisterUseCase {

    private final ClientMasterRepository clientMasterRepository;

    public ClientRegisterService(ClientMasterRepository clientMasterRepository) {
        this.clientMasterRepository = clientMasterRepository;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        ClientMaster clientMaster = new ClientMaster();
        clientMaster.setClientCode(registerRequest.getClientCode());
        clientMaster.setBusinessCode(registerRequest.getBusinessCode());
        clientMaster.setClientName(registerRequest.getClientName());
        clientMaster.setClientBusinessNumber(registerRequest.getClientBusinessNumber());
        clientMaster.setMainPhoneEncrypted(registerRequest.getMainPhoneEncrypted());
        clientMaster.setFaxNumber(registerRequest.getFaxNumber());
        clientMaster.setCeoName(registerRequest.getCeoName());
        clientMaster.setManagerName(registerRequest.getManagerName());
        clientMaster.setManagerEmailEncrypted(registerRequest.getManagerEmailEncrypted());
        clientMaster.setManagerMobileEncrypted(registerRequest.getManagerMobileEncrypted());
        clientMaster.setZipcode(registerRequest.getZipcode());
        clientMaster.setAddressBase(registerRequest.getAddressBase());
        clientMaster.setAddressDetail(registerRequest.getAddressDetail());
        clientMaster.setRemarks(registerRequest.getRemarks());

        clientMasterRepository.save(clientMaster);

        return new RegisterResponse(true, "화주등록 성공!");
    }
}
