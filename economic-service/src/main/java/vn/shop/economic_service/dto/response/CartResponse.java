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
    float total_money;
    Set<ProductCartBuilder> products;

    @Getter
    public static class ProductCartBuilder{
        private String name;
        private float prices;
        private String images;
        private String size;
        private int amount;

        public void setName(String name) {
            this.name = name;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public void setPrices(float prices) {
            this.prices = prices;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

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
