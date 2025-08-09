package msa.productservice.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchCommand {
    private Long clientCode;  // optional
    private String keyword;      // optional
    private int page;
    private int size;
}