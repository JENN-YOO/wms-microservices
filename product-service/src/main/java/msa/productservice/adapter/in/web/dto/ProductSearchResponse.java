package msa.productservice.adapter.in.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductSearchResponse {
    private Long productCode;
    private Long clientCode;
    private String clientName;
    private String productName;
    private String brand;
    private String style;
    private String color;
    private String size;
    private BigDecimal retailPrice;
}
