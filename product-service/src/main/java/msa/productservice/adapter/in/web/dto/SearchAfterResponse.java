package msa.productservice.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchAfterResponse {
    private List<ProductSearchResponse> items;
    private String next; // 다음 페이지 토큰 (없으면 null)
}
