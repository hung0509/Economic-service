package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.CategoryRequest;
import vn.shop.economic_service.dto.response.CategoryResponse;
import vn.shop.economic_service.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    @Mapping(target = "quantity", ignore = true)
    CategoryResponse toCategoryResponse(Category category);
}
