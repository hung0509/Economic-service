package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.shop.economic_service.dto.request.PermissionRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.PermissionResponse;
import vn.shop.economic_service.service.PermissionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionResponse> create(@PathVariable("{id}") String id){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.get(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("{id}") String id){
        permissionService.delete(id);
        return ApiResponse.<String>builder()
                .result("Successfully!")
                .build();
    }
}
