package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.ProductRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.ProductResponse;
import vn.shop.economic_service.service.ProductService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class    ProductController {
    ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> create(@ModelAttribute ProductRequest request) throws IOException {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable String id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.get(id))
                .build();
    }
}
