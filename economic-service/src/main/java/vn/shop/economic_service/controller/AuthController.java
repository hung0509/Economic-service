package vn.shop.economic_service.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shop.economic_service.dto.request.AuthenticateRequest;
import vn.shop.economic_service.dto.request.IntrospectRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.dto.response.AuthenticateResponse;
import vn.shop.economic_service.dto.response.IntrospectResponse;
import vn.shop.economic_service.service.AuthenticationService;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticateResponse> login(@RequestBody AuthenticateRequest request){
        return ApiResponse.<AuthenticateResponse>builder()
                .result(authenticationService.authenticate(request))
                .message("Ok")
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }
}
