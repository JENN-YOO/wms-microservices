package msa.productservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.application.port.in.ProductSearchCommand;
import msa.productservice.application.port.in.ProductSearchUseCase;
import msa.productservice.application.port.out.ProductSearchIndexPort;
import msa.productservice.application.port.out.ProductSearchQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSearchService implements ProductSearchUseCase {

    private final ProductSearchQueryPort searchQueryPort;
    private final ProductSearchIndexPort searchIndexPort;

    @Override
    public Page<ProductSearchResponse> search(ProductSearchCommand command) {
        return searchQueryPort.search(
                command.getClientCode(),
                command.getKeyword(),
                command.getPage(),
                command.getSize()
        );
    }

    @Override
    @Transactional
    public void reindexByProductCode(Long productCode) {
        searchIndexPort.upsertByProductCode(productCode);
    }

    @Override
    @Transactional
    public void reindexByClientCode(Long clientCode) {
        searchIndexPort.updateClientFields(clientCode);
    }
}
