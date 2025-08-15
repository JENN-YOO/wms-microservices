package msa.productservice.application.port.out;

import jakarta.annotation.Nullable;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.adapter.out.search.ElasticsearchProductSearchAdapter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductSearchQueryPort {
    Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size);
    ElasticsearchProductSearchAdapter.SearchPage<ProductSearchResponse> searchAfter(
            Long clientCode, String keyword, int size, @Nullable List<Object> afterSortValues);
}
