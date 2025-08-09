package msa.productservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.application.port.in.ProductSearchCommand;
import msa.productservice.application.port.in.ProductSearchUseCase;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchUseCase productSearchUseCase;

    @GetMapping
    public Page<ProductSearchResponse> search(
            @RequestParam(required = false) Long clientCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var cmd = ProductSearchCommand.builder()
                .clientCode(clientCode)
                .keyword(keyword)
                .page(page)
                .size(size)
                .build();
        return productSearchUseCase.search(cmd);
    }
}
