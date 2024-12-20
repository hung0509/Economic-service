package vn.shop.economic_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.key.CartProductKey;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProduct {
    @EmbeddedId
    CartProductKey id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    Cart cart_pr;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product_pr;

    int amount;
}
