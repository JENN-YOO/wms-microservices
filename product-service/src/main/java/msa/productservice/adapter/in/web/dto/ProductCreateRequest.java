package msa.productservice.adapter.in.web.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {
    private Long clientCode;
    private String productType;
    private String productRole;
    private String supplierCode;
    private String productName;
    private String productGroup;
    private String productYear;
    private String productSeason;
    private String brand;
    private String style;
    private String color;
    private String size;
    private String productSku;
    private String productOrigin;
    private String productUnit;
    private String storageTemperature;
    private BigDecimal retailPrice;
    private String deliveryType;
    private String remarks;
    private String expireDateUseYn;
    private String salePeriodType;
    private String useYn;
    private BigDecimal purchasePrice;
}
