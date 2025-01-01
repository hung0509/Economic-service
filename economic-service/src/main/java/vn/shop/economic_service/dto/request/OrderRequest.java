package vn.shop.economic_service.dto.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.DeliveryMethod;
import vn.shop.economic_service.entity.OrderDetail;
import vn.shop.economic_service.entity.PaymentMethod;
import vn.shop.economic_service.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String andress;
    float total_money;
    int delivery_id;
    int payment_id;
    List<OrderDetailRequest> orderDetails;
    String user_id;
}
