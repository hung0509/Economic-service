package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.AndressRequest;
import vn.shop.economic_service.dto.response.AndressResonse;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.service.AndressService;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/andress")
public class AndressController {
    AndressService andressService;

    @PutMapping("/{id}")
    public ApiResponse<AndressResonse> update(@PathVariable String id, @RequestBody AndressRequest request) {
        return ApiResponse.<AndressResonse>builder()
                .result(andressService.update(id, request))
                .build();
    }


}
