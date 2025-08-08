package msa.productservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
                String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null) {
                    template.header(HttpHeaders.AUTHORIZATION, authorization);
                }
            }
        };
    }
}
