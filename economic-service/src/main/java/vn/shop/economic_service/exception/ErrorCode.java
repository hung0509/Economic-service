package vn.shop.economic_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_EXIST(1001, "User not exist", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXIST(1002, "Role not exist", HttpStatus.BAD_REQUEST),
    ANDRESS_NOT_EXIST(1003, "Andress not exist", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_EXIST(1004, "image not exist", HttpStatus.BAD_REQUEST),
    DISCOUNT_NOT_EXIST(1005, "User not exist", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXIST(1006, "Category not exist", HttpStatus.BAD_REQUEST),
    SIZE_NOT_EXIST(1007, "Size not exist", HttpStatus.BAD_REQUEST),
    CART_NOT_EXIST(1008, "Cart not exist", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXIST(1009, "Product not exist", HttpStatus.BAD_REQUEST),
    DELIVERY_METHOD_NOT_EXIST(1011, "Delivery method not exist", HttpStatus.BAD_REQUEST),
    PAYMENT_METHOD_NOT_EXIST(1012, "Payment method not exist", HttpStatus.BAD_REQUEST),
    PAYMENT_CREATE_ERROR(1013, "Payment create error", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ENOUGH(1010, "Product not enough", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(9999, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NOT_VALID_FORMAT_IMAGE(2001, "This file is not image", HttpStatus.BAD_REQUEST),
    EXISTED_USERNAME(4001, "Username already existed", HttpStatus.BAD_REQUEST),
    EXISTED_EMAIL(4002, "Email already existed", HttpStatus.BAD_REQUEST),
    LINK_EXPIRE(8888, "Link eprired", HttpStatus.BAD_REQUEST),
    DECODE_NOT_AVAILABLE(8887, "Decode not available", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatus status;
}
