package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.dto.request.OrderDetailRequest;
import vn.shop.economic_service.entity.DeliveryMethod;
import vn.shop.economic_service.entity.OrderDetail;
import vn.shop.economic_service.entity.PaymentMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String andress;
    float total_money;
    String status_payment;
    String status_delivery;
    LocalDate order_date;
    String delivery_name;
    String payment_name;
    Set<OrderDetail> orderDetails;
    String fullname;
}
