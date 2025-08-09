package msa.productservice.adapter.in.web.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWithClientDto {
    private Long productCode;
    private String productName;
    private Long clientCode;
    private String clientName;
}
