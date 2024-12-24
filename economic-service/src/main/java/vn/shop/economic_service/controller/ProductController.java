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
import java.util.List;

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

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable String id
            ,@ModelAttribute ProductRequest request) throws IOException {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.update(request, id))
                .build();
    }

    @GetMapping("/sales")
    public ApiResponse<List<ProductResponse>> getProductSale(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "8") int size){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllByDiscount(page, size))
                .build();
    }

    @GetMapping("/category/{name}")
    public ApiResponse<List<ProductResponse>> getProductCategory(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "8") int size,
                                                                 @PathVariable String name){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getByCategory(name, page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable String id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.get(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAll(page, size))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        productService.remove(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}
