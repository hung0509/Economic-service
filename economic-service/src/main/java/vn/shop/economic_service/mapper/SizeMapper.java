package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import vn.shop.economic_service.dto.request.SizeRequest;
import vn.shop.economic_service.dto.response.SizeResponse;
import vn.shop.economic_service.entity.Size;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    Size toSize(SizeRequest request);

    SizeResponse toSizeResponse(Size size);
}
