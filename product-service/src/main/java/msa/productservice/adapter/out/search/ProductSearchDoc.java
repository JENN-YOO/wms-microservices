package msa.productservice.adapter.out.search;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document(indexName = "product_search_v1")
@Routing("clientCode")
public class ProductSearchDoc {
    @Id
    private Long productCode;

    private Long   clientCode;
    private String clientName;

    private String productName;
    private String brand;
    private String style;
    private String color;
    private String size;
    private String productYear;
    private String productSeason;

    private BigDecimal retailPrice;
    private String  useYn;

    private String  keywords;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Instant lastEventAt;

    private Long    version;
}
