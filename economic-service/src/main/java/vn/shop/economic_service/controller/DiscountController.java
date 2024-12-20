package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.DiscountRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.DiscountResponse;
import vn.shop.economic_service.service.DiscountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/discounts")
public class DiscountController {
    DiscountService discountService;

    @PostMapping
    public ApiResponse<DiscountResponse> create(@RequestBody DiscountRequest request) {
        return ApiResponse.<DiscountResponse>builder()
                .result(discountService.create(request))
                .message("Create discount successfully!!!")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DiscountResponse> getById(@PathVariable String id) {
        return ApiResponse.<DiscountResponse>builder()
                .result(discountService.get(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<DiscountResponse>> get(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<List<DiscountResponse>>builder()
                .result(discountService.getAll(page, size))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        discountService.delete(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/count")
    public ApiResponse<Long> count(){
        return ApiResponse.<Long>builder()
                .result(discountService.count())
                .build();
    }
}
