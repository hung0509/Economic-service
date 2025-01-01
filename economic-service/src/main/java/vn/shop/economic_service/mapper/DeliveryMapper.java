package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import vn.shop.economic_service.dto.request.DeliveryRequest;
import vn.shop.economic_service.dto.response.DeliveryResponse;
import vn.shop.economic_service.entity.DeliveryMethod;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryMethod toDeliveryMethod(DeliveryRequest request);

    DeliveryResponse toDeliveryResponse(DeliveryMethod deliveryMethod);
}
