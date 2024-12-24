package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.CartProductResquest;
import vn.shop.economic_service.dto.response.CartProductResponse;
import vn.shop.economic_service.entity.CartProduct;

@Mapper(componentModel = "spring")
public interface CartProductMapper {
    @Mapping(target = "product_pr", ignore = true)
    @Mapping(target = "cart_pr", ignore = true)
    CartProduct toCartProduct(CartProductResquest resquest);

    @Mapping(target = "productCart.name", source = "product_pr.name")
    @Mapping(target = "productCart.images", source = "product_pr.images")
    @Mapping(target = "productCart.prices", source = "product_pr.price")
    @Mapping(target = "cart_pr_id", source = "cart_pr.id")
    CartProductResponse toCartProductResponse(CartProduct cartProduct);
}
