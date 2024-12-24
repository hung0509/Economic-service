package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.ProductRequest;
import vn.shop.economic_service.dto.response.ProductResponse;
import vn.shop.economic_service.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "sizes", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "sizes.id", ignore = true)
    ProductResponse toProductResponse(Product product);
}
