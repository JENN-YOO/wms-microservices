package msa.productservice.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductSearchRequest;
import msa.productservice.adapter.in.web.dto.ProductSearchResponse;
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
}
