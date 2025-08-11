package msa.productservice.application.port.in;


public interface ProductIndexCommandUseCase {
    void reindexByProductCode(Long productCode, Long eventVersion);
    void reindexByClientCode(Long clientCode);
}
