package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.UserCreateRequest;
import vn.shop.economic_service.dto.request.UserUpdateRequest;
import vn.shop.economic_service.dto.response.UserResponse;
import vn.shop.economic_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toCreateUser(UserCreateRequest request);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "andress", ignore = true)
    User toUpdateUser(UserUpdateRequest request);

    @Mapping(target = "cart_id", source = "cart.id")
    @Mapping(target = "orders", ignore = true)
    UserResponse toUserResponse(User user);
}
