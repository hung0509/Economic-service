package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import vn.shop.economic_service.dto.request.OrderDetailRequest;
import vn.shop.economic_service.entity.OrderDetail;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    OrderDetail toOrderDetail(OrderDetailRequest request);
}
