package msa.productservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductSearchRequest;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
import msa.productservice.adapter.in.web.dto.SearchAfterResponse;
import msa.productservice.application.port.in.ProductQueryUseCase;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class ProductSearchController {


    private final ProductQueryUseCase productQueryUseCase;

    @GetMapping
    public Page<ProductSearchResponse> search(@ModelAttribute ProductSearchRequest req) {
        return productQueryUseCase.search(
                req.getClientCode(),
                req.getKeyword(),   // InitBinder로 공백이면 null
                req.getPage(),
                req.getSize()
        );
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    // ✅ 딥 페이지네이션용
    @GetMapping("/after")
    public SearchAfterResponse searchAfter(@RequestParam(required = false) Long clientCode,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(defaultValue = "50") int size,
                                           @RequestParam(required = false, name = "after") String afterToken) {
        int s = Math.min(Math.max(size, 1), 100);
        return productQueryUseCase.searchAfter(clientCode, keyword, s, afterToken);
    }

}
