package vn.shop.economic_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.shop.economic_service.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(exception = AppException.class)
    public ResponseEntity<ApiResponse<AppException>> handlerAppException(AppException appException){
        ErrorCode errorCode = appException.getErrorCode();
        ApiResponse<AppException> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus())
                .body(apiResponse);
    }
}
