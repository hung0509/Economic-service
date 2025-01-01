package vn.shop.economic_service.dto.response;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.CartProduct;
import vn.shop.economic_service.entity.User;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    String id;
    User user;
    Set<ProductCartBuilder> products;

    @Getter
    @Setter
    public static class ProductCartBuilder{
        private String id;
        private String name;
        private float prices;
        private String images;
        private String size;
        private int amount;

        public ProductCartBuilder(String name, String images, String size, float prices, int amount) {
            this.name = name;
            this.images = images;
            this.size = size;
            this.prices = prices;
            this.amount = amount;
        }

        public ProductCartBuilder() {
        }
    }
}
