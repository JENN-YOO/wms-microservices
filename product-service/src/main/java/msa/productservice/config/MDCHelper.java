package msa.productservice.config;

import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MDCHelper {
    public static final String REQUEST_ID = "requestId";
    public static final String DEBUG = "debug";

    public static void init(String requestId) {
        clear();
        MDC.put(REQUEST_ID, requestId);
    }

    public static void appendDebug(Class<?> clazz, String message) {
        String debugMsg = MDC.get(DEBUG) != null ? MDC.get(DEBUG) : "";
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        MDC.put(DEBUG, debugMsg + "\n" + time + " " + clazz.getSimpleName() + " " + message);
    }

    public static String getDebug() {
        return MDC.get(DEBUG);
    }

    public static void clear() {
        MDC.clear();
    }

    // metadata로 로그에 추가할 map 반환
    public static java.util.Map<String, Object> getMetadata() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put(DEBUG, getDebug());
        return map;
    }
}
