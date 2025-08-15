package msa.productservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductCreateRequest;
import msa.productservice.adapter.in.web.dto.ProductResponse;
import msa.productservice.adapter.in.web.dto.ProductWithClientDto;
import msa.productservice.application.port.in.ProductCommandUseCase;
import msa.productservice.application.port.in.ProductQueryUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductCommandUseCase productCommandUseCase;
    private final ProductQueryUseCase productQueryUseCase;


    @GetMapping("/with-client")
    public ResponseEntity<List<ProductWithClientDto>> getProductsWithClient() {
        List<ProductWithClientDto> result = productQueryUseCase.getProductsWithClientName();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<ProductResponse> registerProduct(
            @RequestBody ProductCreateRequest productCreateRequest,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : "anonymous";
        logger.info("[상품등록][요청자: {}] 상품명: {}", username, productCreateRequest.getProductName());

        Optional<String> role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.equals("ROLE_ADMIN"))
                .map(auth -> auth.replace("ROLE_", ""))
                .findFirst();
        String roleName = role.orElse("");

        try {
            ProductResponse response = productCommandUseCase.registerProduct(productCreateRequest, username, roleName);

            // 상품명 중복 등 실패 응답 케이스
            if (!response.isSuccess()) {
                logger.warn("[상품등록][실패][요청자: {}] 사유: {}", username, response.getMessage());
                // 보통 중복이면 409(CONFLICT) 반환
                return ResponseEntity
                        .status(409)
                        .body(response);
            }

            logger.info("[상품등록][성공][요청자: {}] 상품명: {}", username, productCreateRequest.getProductName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("[상품등록][예외][요청자: {}] 상품명: {} - {}", username, productCreateRequest.getProductName(), e.getMessage(), e);
            // 예상 못한 에러는 500 반환
            return ResponseEntity
                    .status(500)
                    .body(ProductResponse.fail(false, "서버 오류가 발생했습니다."));
        }
    }

    @GetMapping("/products-with-clients")
    public Page<ProductWithClientDto> getProductsWithClients(
            @RequestParam(required = false) Long clientCode,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return productQueryUseCase.getProductsWithClients(clientCode, keyword, pageable);
    }

}

