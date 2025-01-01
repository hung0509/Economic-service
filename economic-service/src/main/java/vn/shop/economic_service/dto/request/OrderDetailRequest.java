package vn.shop.economic_service.dto.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Order;
import vn.shop.economic_service.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    int quantity;
    String size;
    String product_id;
}
