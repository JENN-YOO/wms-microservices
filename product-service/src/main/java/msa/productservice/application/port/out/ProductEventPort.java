package msa.productservice.application.port.out;

import java.time.Instant;

public interface ProductEventPort {
    void publishProductCreated(long productCode, Instant occurredAt, long version);
    // TODO void publishProductUpdated(...)
}
