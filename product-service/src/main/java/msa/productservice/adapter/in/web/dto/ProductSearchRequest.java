package msa.productservice.adapter.in.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductSearchRequest {
    private Long clientCode;
    private String keyword;
    private int page = 0;
    private int size = 20; // 기본값
}