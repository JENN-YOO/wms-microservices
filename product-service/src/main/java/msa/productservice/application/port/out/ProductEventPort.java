package msa.productservice.application.port.out;

import java.time.Instant;

public interface ProductEventPort {
    void publishProductCreated(long productCode, Instant occurredAt, long version);
    // 필요 시: void publishProductUpdated(...)
}
