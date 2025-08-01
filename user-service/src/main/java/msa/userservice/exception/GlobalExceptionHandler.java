package msa.userservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger("ELK_LOGGER");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {

        System.err.println("Unhandled exception occurred: " + ex.getMessage());
        // ELK로 에러 로그 남기기
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("exceptionMessage", ex.getMessage());

        logger.error("UNHANDLED_EXCEPTION", StructuredArguments.fields(logMap), ex);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred. Please contact support.");

        // 개발 환경에서만 상세 메시지 추가
        // body.put("exception_message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
