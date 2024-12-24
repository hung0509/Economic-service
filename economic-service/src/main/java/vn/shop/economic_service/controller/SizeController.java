package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.SizeRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.SizeResponse;
import vn.shop.economic_service.service.SizeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/sizes")
public class SizeController {
    SizeService  sizeService;
    @PostMapping
    public ApiResponse<SizeResponse> create(@RequestBody SizeRequest request){
        return ApiResponse.<SizeResponse>builder()
                .result(sizeService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SizeResponse>> get(){
        return ApiResponse.<List<SizeResponse>>builder()
                .result(sizeService.getAll())
                .build();
    }
}
