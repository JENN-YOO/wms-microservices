package msa.productservice.adapter.in.web.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductWithClientDto {
    private Long productCode;
    private String productName;
    private String brand;
    private BigDecimal retailPrice;

    private Integer clientCode;   // ClientMaster의 clientCode가 int 이므로 Integer 유지
    private String clientName;
    private String businessCode;
}
