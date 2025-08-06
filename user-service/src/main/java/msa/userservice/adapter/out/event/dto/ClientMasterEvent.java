package msa.userservice.adapter.out.event.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientMasterEvent {
    private int clientCode;
    private String clientName;
    private String serviceStatus;
    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;
}
