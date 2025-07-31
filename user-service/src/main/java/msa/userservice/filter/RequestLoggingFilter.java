package msa.userservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import msa.userservice.adapter.in.web.dto.RequestLog;
import msa.userservice.config.MDCHelper;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger("ELK_LOGGER"); // logback에서 ELK_LOGGER로 지정

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 고유 requestId 생성 및 MDC/ThreadContext 등록
        String requestId = UUID.randomUUID().toString();
        MDCHelper.init(requestId);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        LocalDateTime requestAt = LocalDateTime.now();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            LocalDateTime responseAt = LocalDateTime.now();
            long elapseTime = Duration.between(requestAt, responseAt).toMillis();

            // 로그 구조화: JSON 형태 (requestId + metadata + ...)
            RequestLog requestLog = RequestLog.of(
                    requestId,
                    wrappedRequest,
                    wrappedResponse,
                    requestAt,
                    responseAt,
                    elapseTime,
                    MDCHelper.getMetadata()
            );
            logger.info("REQUEST_LOG", StructuredArguments.fields(requestLog));

            wrappedResponse.copyBodyToResponse();
            MDCHelper.clear();
        }
    }
}
