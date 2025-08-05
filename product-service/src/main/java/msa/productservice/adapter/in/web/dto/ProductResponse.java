package msa.productservice.adapter.in.web.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private boolean success;
    private String message;
    private ProductRequest data;  // 상품 핵심 정보

    public static ProductResponse success(String message, ProductRequest dto) {
        return ProductResponse.builder()
                .success(true)
                .message(message)
                .data(dto)
                .build();
    }
    public static ProductResponse fail(boolean result,String message) {
        return ProductResponse.builder()
                .success(result)
                .message(message)
                .build();
    }

    public static ProductResponse success(boolean result, String message) {
        return ProductResponse.builder()
                .success(true)
                .message(message)
                .build();
    }
}
