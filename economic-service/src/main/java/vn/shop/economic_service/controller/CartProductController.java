package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.CartResponse;
import vn.shop.economic_service.entity.CartProduct;
import vn.shop.economic_service.service.CartProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cart-products")
public class CartProductController {
    CartProductService cartProductService;

    @GetMapping
    public ApiResponse<List<CartProduct>> getAllCartProducts() {
        return ApiResponse.<List<CartProduct>>builder()
                .result(cartProductService.getAllCartProduct())
                .build();
    }
}
