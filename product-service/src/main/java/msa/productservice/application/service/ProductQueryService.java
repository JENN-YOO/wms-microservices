package msa.productservice.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.adapter.in.web.dto.ProductWithClientDto;
import msa.productservice.adapter.in.web.dto.SearchAfterResponse;
import msa.productservice.adapter.out.FeignClient.UserServiceClient;
import msa.productservice.adapter.out.persistence.ProductMasterRepository;
import msa.productservice.application.port.in.ProductQueryUseCase;
import msa.productservice.application.port.out.ProductSearchIndexPort;
import msa.productservice.application.port.out.ProductSearchQueryPort;
import msa.productservice.domain.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductQueryService implements ProductQueryUseCase {

    private final ProductMasterRepository productMasterRepository;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper om = new ObjectMapper();
    private final ProductSearchQueryPort queryPort;

    @Override
    @Transactional(readOnly = true)
    public List<ProductWithClientDto> getProductsWithClientName() {
        List<ProductMaster> products = productMasterRepository.findAll();

        return products.stream().map(product -> {
            String clientName = "";
            try {
                clientName = userServiceClient.getClientName(product.getClientCode().longValue());

            } catch (Exception e) {
                clientName = "조회실패";
            }
            return ProductWithClientDto.builder()
                    .productCode(product.getProductCode())
                    .productName(product.getProductName())
                    .clientCode(Math.toIntExact(product.getClientCode()))
                    .clientName(clientName)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Page<ProductSearchResponse> search(Long clientCode, String keyword, int page, int size) {
        int p = Math.max(page, 0);
        int s = Math.min(Math.max(size, 1), 100);
        String k = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        return queryPort.search(clientCode, k, p, s);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductWithClientDto> getProductsWithClients(Long clientCode, String keyword, Pageable pageable) {
        String k = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return productMasterRepository.searchProductsWithClients(clientCode, k, pageable);
    }


    // === search_after 지원 ===
    @Override
    public SearchAfterResponse searchAfter(Long clientCode, String keyword, int size, @Nullable String afterToken) {
        String k = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        List<Object> after = decodeToken(afterToken); // null 허용
        var page = queryPort.searchAfter(clientCode, k, size, after);
        String next = encodeToken(page.getNextToken());
        return new SearchAfterResponse(page.getContents(), next);
    }

    private String encodeToken(@Nullable List<Object> sortValues) {
        if (sortValues == null || sortValues.isEmpty()) return null;
        try {
            byte[] json = om.writeValueAsBytes(sortValues);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
        } catch (Exception e) { return null; }
    }

    private List<Object> decodeToken(@Nullable String token) {
        if (token == null || token.isBlank()) return null;
        try {
            byte[] json = Base64.getUrlDecoder().decode(token);
            return om.readValue(json, new TypeReference<List<Object>>() {});
        } catch (Exception e) { return null; }
    }
}
