package msa.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceApplication.class);
    public static void main(String[] args) {
        logger.info("유지원 테스트");
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
