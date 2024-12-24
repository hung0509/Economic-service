package vn.shop.economic_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Cart;
import vn.shop.economic_service.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProductResquest {
    String product_id; //id
    String size;
    String cart_id; // của cart nào
    int amount; //số lượng lấy ra
}
