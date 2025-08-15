package msa.productservice.adapter.in.web.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductWithClientDto {
    private Long productCode;
    private String productName;
    private String brand;
    private BigDecimal retailPrice;

    private int clientCode;
    private String clientName;
    private String businessCode;

}
