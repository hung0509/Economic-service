package vn.shop.economic_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.shop.economic_service.dto.request.UserCreateRequest;
import vn.shop.economic_service.dto.request.UserUpdateRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.UserResponse;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.update(id,request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> get(@PathVariable String id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.get(id))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> get(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable String id){
        userService.delete(id);
        return ApiResponse.<String>builder()
                .result("Success")
                .build();
    }
    
    @GetMapping("/info/{username}")
    public ApiResponse<UserResponse> getByUsername(@PathVariable String username){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getByUsername(username))
                .build();
    }

    @GetMapping("/customer")
    public ApiResponse<List<UserResponse>> getCustomer(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getCustomer("USER",page, size))
                .build();
    }

    @GetMapping("verify/{token}")
    public RedirectView verifyEmail(@PathVariable String token){
        try{
            userService.verifyAccount(token);
        }catch (AppException e){
            if(e.getErrorCode().getCode() == 8888 || e.getErrorCode().getCode() == 1001){
                return new RedirectView("http://localhost:4200/404");
            }
        }
        return new RedirectView("http://localhost:4200/");
    }

    @GetMapping("reset-password/{email}")
    public ApiResponse<Void> resetPassword(@PathVariable String email){
        userService.SendMailResetPassword(email);
        return ApiResponse.<Void>builder()
                .build();
    }
}
