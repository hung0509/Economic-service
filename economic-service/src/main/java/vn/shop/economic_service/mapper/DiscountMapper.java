package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.DiscountRequest;
import vn.shop.economic_service.dto.response.DiscountResponse;
import vn.shop.economic_service.entity.Discount;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    Discount toDiscount(DiscountRequest request);

    DiscountResponse toDiscountResponse(Discount discount);
}
