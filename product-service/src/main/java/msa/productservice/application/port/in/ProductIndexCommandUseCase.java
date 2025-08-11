package msa.productservice.application.port.in;


public interface ProductIndexCommandUseCase {
    void reindexByProductCode(Long productCode);
    void reindexByClientCode(Long clientCode);
}
