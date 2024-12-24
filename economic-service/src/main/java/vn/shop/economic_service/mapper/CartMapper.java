package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.response.CartResponse;
import vn.shop.economic_service.entity.Cart;
import vn.shop.economic_service.entity.CartProduct;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "user.roles", ignore = true)
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.orders", ignore = true)
    @Mapping(target = "user.username", ignore = true)
    CartResponse toCartResponse(Cart cart);

    @Mapping(source = "product_pr.name", target = "name")
    @Mapping(source = "product_pr.price", target = "prices")
    @Mapping(source = "product_pr.images", target = "images")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "amount", target = "amount")
    CartResponse.ProductCartBuilder cartProductToProductCartBuilder(CartProduct cartProduct);
}
