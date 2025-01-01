package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.OrderRequest;
import vn.shop.economic_service.dto.response.OrderResponse;
import vn.shop.economic_service.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderDetails", ignore = true)
    Order toOrder(OrderRequest request);

    @Mapping(target = "delivery_name", source = "deliveryMethod.name")
    @Mapping(target = "payment_name", source = "paymentMethod.name")
    @Mapping(target = "fullname",ignore = true)
    OrderResponse toOrderResponse(Order order);
}

