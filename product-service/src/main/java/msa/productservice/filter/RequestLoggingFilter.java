package msa.productservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import msa.productservice.config.MDCHelper;
import msa.productservice.adapter.in.web.dto.RequestLog;
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

    private static final Logger logger = LoggerFactory.getLogger("ELK_LOGGER");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        MDCHelper.init(requestId);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        LocalDateTime requestAt = LocalDateTime.now();
        Exception caughtException = null;

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Exception ex) {
            caughtException = ex;
            throw ex;
        } finally {
            LocalDateTime responseAt = LocalDateTime.now();
            long elapseTime = Duration.between(requestAt, responseAt).toMillis();

            RequestLog requestLog = RequestLog.of(
                    requestId,
                    wrappedRequest,
                    wrappedResponse,
                    requestAt,
                    responseAt,
                    elapseTime,
                    MDCHelper.getMetadata(),
                    (caughtException != null) ? getStackTraceAsString(caughtException) : null
            );

            if (caughtException != null) {
                logger.error("REQUEST_LOG",
                        StructuredArguments.keyValue("requestId", requestLog.getRequestId()),
                        StructuredArguments.keyValue("request", requestLog.getRequest()),
                        StructuredArguments.keyValue("response", requestLog.getResponse()),
                        StructuredArguments.keyValue("metadata", requestLog.getMetadata()),
                        StructuredArguments.keyValue("exception", requestLog.getException())
                );
            } else {
                logger.info("REQUEST_LOG",
                        StructuredArguments.keyValue("requestId", requestLog.getRequestId()),
                        StructuredArguments.keyValue("request", requestLog.getRequest()),
                        StructuredArguments.keyValue("response", requestLog.getResponse()),
                        StructuredArguments.keyValue("metadata", requestLog.getMetadata()),
                        StructuredArguments.keyValue("exception", requestLog.getException())
                );
            }

            wrappedResponse.copyBodyToResponse();
            MDCHelper.clear();
        }
    }

    private static String getStackTraceAsString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\n");
        for (StackTraceElement elem : ex.getStackTrace()) {
            sb.append("\tat ").append(elem).append("\n");
        }
        return sb.toString();
    }
}
