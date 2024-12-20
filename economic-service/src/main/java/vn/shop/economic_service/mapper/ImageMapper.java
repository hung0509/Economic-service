package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import vn.shop.economic_service.dto.response.ImageResponse;
import vn.shop.economic_service.entity.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponse toImageResponse(Image image);
}
