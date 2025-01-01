package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.PaymentMethodRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.PaymentMethodResonse;
import vn.shop.economic_service.service.PaymentMethodService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/payment-method")
public class PaymentMethodController {
    PaymentMethodService paymentMethodService;

    @PostMapping
    public ApiResponse<PaymentMethodResonse> create(@RequestBody PaymentMethodRequest request) {
        return ApiResponse.<PaymentMethodResonse>builder()
                .result(paymentMethodService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PaymentMethodResonse>> get() {
        return ApiResponse.<List<PaymentMethodResonse>>builder()
                .result(paymentMethodService.getAll())
                .build();
    }
}
