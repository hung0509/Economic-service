package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.DeliveryRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.DeliveryResponse;
import vn.shop.economic_service.dto.response.PaymentMethodResonse;
import vn.shop.economic_service.service.DeliveryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/delivery")
public class DeliveryController {
    DeliveryService deliveryService;

    @PostMapping
    public ApiResponse<DeliveryResponse> create(@RequestBody DeliveryRequest request){
        return ApiResponse.<DeliveryResponse>builder()
                .result(deliveryService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<DeliveryResponse>> get() {
        return ApiResponse.<List<DeliveryResponse>>builder()
                .result(deliveryService.getAll())
                .build();
    }
}
