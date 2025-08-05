package msa.productservice.domain;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_code")
    private Long productCode;   // 오토 인크리즈 PK

    @Column(name = "client_code", nullable = false, length = 24)
    private String clientCode;

    @Column(name = "product_type", nullable = false, length = 1)
    private String productType;

    @Column(name = "product_role", nullable = false, length = 2)
    private String productRole;

    @Column(name = "supplier_code", length = 24)
    private String supplierCode;

    @Column(name = "product_name", columnDefinition = "TEXT")
    private String productName;

    @Column(name = "product_group", length = 50)
    private String productGroup;

    @Column(name = "product_year", length = 10)
    private String productYear;

    @Column(name = "product_season", length = 50)
    private String productSeason;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "style", length = 50)
    private String style;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "product_sku", length = 50)
    private String productSku;

    @Column(name = "product_origin", length = 50)
    private String productOrigin;

    @Column(name = "product_unit", length = 20)
    private String productUnit;

    @Column(name = "storage_temperature", length = 10)
    private String storageTemperature;

    @Column(name = "retail_price", precision = 10, scale = 2)
    private BigDecimal retailPrice;

    @Column(name = "delivery_type", length = 2)
    private String deliveryType;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "expire_date_use_yn", length = 1)
    private String expireDateUseYn;

    @Column(name = "sale_period_type", length = 1)
    private String salePeriodType;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "product_register_type", length = 1)
    private String productRegisterType;

    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    @Column(name = "register_id", length = 48)
    private String registerId;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;
}
