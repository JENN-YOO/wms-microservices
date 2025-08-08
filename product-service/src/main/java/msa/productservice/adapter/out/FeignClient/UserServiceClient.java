package msa.productservice.adapter.out.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {
    @GetMapping("/client/{clientCode}/name")
    String getClientName(@PathVariable("clientCode") int clientCode);
}

