package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import vn.shop.economic_service.dto.request.PaymentMethodRequest;
import vn.shop.economic_service.dto.response.PaymentMethodResonse;
import vn.shop.economic_service.entity.PaymentMethod;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMethod toPaymentMethod(PaymentMethodRequest request);

    PaymentMethodResonse toPaymentResonse(PaymentMethod paymentMethod);
}
