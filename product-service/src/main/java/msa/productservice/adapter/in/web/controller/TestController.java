package msa.productservice.adapter.in.web.controller;

import msa.productservice.config.MDCHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> test(Authentication authentication) {
        Map<String, Object> mdcMetadata = MDCHelper.getMetadata();
        if (authentication == null) {
            return Map.of(
                    "authenticated", false,
                    "message", "No authentication found",
                    "mdcMetadata", mdcMetadata
            );
        }
        return Map.of(
                "authenticated", authentication.isAuthenticated(),
                "name", authentication.getName(),
                "principal", String.valueOf(authentication.getPrincipal()),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                "credentials", String.valueOf(authentication.getCredentials()),
                "details", String.valueOf(authentication.getDetails()),
                "class", authentication.getClass().getName(),
                "mdcMetadata", mdcMetadata
        );
    }
}
