package msa.productservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.adapter.in.web.dto.ProductWithClientDto;
import msa.productservice.adapter.out.FeignClient.UserServiceClient;
import msa.productservice.adapter.out.persistence.ProductMasterRepository;
import msa.productservice.application.port.in.ProductQueryUseCase;
import msa.productservice.application.port.out.ProductSearchIndexPort;
import msa.productservice.application.port.out.ProductSearchQueryPort;
import msa.productservice.domain.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductQueryService implements ProductQueryUseCase {

    private final ProductMasterRepository productMasterRepository;
    private final UserServiceClient userServiceClient;

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
                    .clientCode(product.getClientCode())
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
}
