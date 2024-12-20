package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.ImageResponse;
import vn.shop.economic_service.service.ImageService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/images")
public class ImageController {
    ImageService imageService;

    @GetMapping
    public ApiResponse<List<ImageResponse>> get(){
        return ApiResponse.<List<ImageResponse>>builder()
                .result(imageService.getAll())
                .build();
    }
}
