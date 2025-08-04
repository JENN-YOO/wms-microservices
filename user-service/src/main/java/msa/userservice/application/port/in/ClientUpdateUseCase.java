package msa.userservice.application.port.in;

import msa.userservice.adapter.in.web.dto.ClientUpdateRequest;
import msa.userservice.adapter.in.web.dto.ClientUpdateResponse;

public interface ClientUpdateUseCase {
    ClientUpdateResponse update(int clientCode, ClientUpdateRequest clientUpdateRequest);
}
