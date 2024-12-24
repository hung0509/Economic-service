package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.CartProductResquest;
import vn.shop.economic_service.dto.request.SizeRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.CartProductResponse;
import vn.shop.economic_service.dto.response.CartResponse;
import vn.shop.economic_service.dto.response.SizeResponse;
import vn.shop.economic_service.service.CartService;
import vn.shop.economic_service.service.SizeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/carts")
public class CartController {
    CartService cartService;

    @GetMapping
    public ApiResponse<List<CartResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "0") int size) {
        return ApiResponse.<List<CartResponse>>builder()
                .result(cartService.getAll(page,size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CartResponse> getById(@PathVariable String id){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCartById(id))
                .build();
    }

    @PostMapping("/add-product")
    public ApiResponse<CartProductResponse> addProduct(@RequestBody  CartProductResquest resquest){
        return ApiResponse.<CartProductResponse>builder()
                .result(cartService.addProductinCart(resquest))
                .build();
    }
}
