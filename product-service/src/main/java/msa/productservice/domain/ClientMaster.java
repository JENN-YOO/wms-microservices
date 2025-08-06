package msa.productservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_client_master")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientMaster {
    @Id
    private int clientCode;
    private String businessCode;
    private String clientName;
    private String clientBusinessNumber;
    private String serviceStatus;
    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;
}
