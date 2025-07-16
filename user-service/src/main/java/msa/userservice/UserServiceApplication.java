package msa.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("msa.userservice.adapter.out.persistence.mybatis") // MyBatis 전용 스캔
@EnableJpaRepositories("msa.userservice.adapter.out.persistence.jpa") // JPA 전용 스캔
public class UserServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        logger.info("이현재 테스트");
    }

}
