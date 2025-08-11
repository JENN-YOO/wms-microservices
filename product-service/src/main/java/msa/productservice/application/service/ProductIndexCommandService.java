package msa.productservice.application.service;

import lombok.RequiredArgsConstructor;
import msa.productservice.application.port.in.ProductIndexCommandUseCase;
import msa.productservice.application.port.out.ProductSearchIndexPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductIndexCommandService implements ProductIndexCommandUseCase {

    private final ProductSearchIndexPort indexPort;

    @Override
    @Transactional
    public void reindexByProductCode(Long productCode) {
        indexPort.upsertByProductCode(productCode);
    }

    @Override
    @Transactional
    public void reindexByClientCode(Long clientCode) {
        indexPort.updateClientFields(clientCode);
    }
}
