package msa.userservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestLog {
    private String requestId;                    // 고유 식별자
    private RequestInfo request;
    private ResponseInfo response;
    private Map<String, Object> metadata;        // MDC 등 동적 필드

    public static RequestLog of(String requestId,
                                ContentCachingRequestWrapper req,
                                ContentCachingResponseWrapper res,
                                LocalDateTime reqAt,
                                LocalDateTime resAt,
                                long elapseTime,
                                Map<String, Object> metadata) {
        return RequestLog.builder()
                .requestId(requestId)
                .request(RequestInfo.of(req, reqAt))
                .response(ResponseInfo.of(res, elapseTime, resAt))
                .metadata(metadata)
                .build();
    }

    @Getter
    @Builder
    public static class RequestInfo {
        private String url;
        private String method;
        private Map<String, String> headers;
        private String body;
        private String requestAt;

        public static RequestInfo of(ContentCachingRequestWrapper req, LocalDateTime reqAt) {
            return RequestInfo.builder()
                    .url(req.getRequestURL().toString())
                    .method(req.getMethod())
                    .headers(getHeaders(req))
                    .body(getBody(req.getContentAsByteArray()))
                    .requestAt(reqAt.toString())
                    .build();
        }
        private static Map<String, String> getHeaders(HttpServletRequest request) {
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                headers.put(name, request.getHeader(name));
            }
            return headers;
        }
        private static String getBody(byte[] buf) {
            return buf.length > 0 ? new String(buf, StandardCharsets.UTF_8) : "";
        }
    }

    @Getter
    @Builder
    public static class ResponseInfo {
        private int status;
        private Map<String, String> headers;
        private String body;
        private int bodySize;
        private long elapseTime;
        private String responseAt;

        public static ResponseInfo of(ContentCachingResponseWrapper res, long elapseTime, LocalDateTime resAt) {
            byte[] buf = res.getContentAsByteArray();
            return ResponseInfo.builder()
                    .status(res.getStatus())
                    .headers(getHeaders(res))
                    .body(getBody(buf))
                    .bodySize(buf.length)
                    .elapseTime(elapseTime)
                    .responseAt(resAt.toString())
                    .build();
        }
        private static Map<String, String> getHeaders(HttpServletResponse response) {
            Map<String, String> headers = new HashMap<>();
            for (String name : response.getHeaderNames()) {
                headers.put(name, response.getHeader(name));
            }
            return headers;
        }
        private static String getBody(byte[] buf) {
            return buf.length > 0 ? new String(buf, StandardCharsets.UTF_8) : "";
        }
    }
}
