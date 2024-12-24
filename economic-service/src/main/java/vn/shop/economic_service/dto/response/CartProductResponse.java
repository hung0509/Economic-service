package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Cart;
import vn.shop.economic_service.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProductResponse {
    String cart_pr_id;
    int amount;
    ProductCart productCart;

    public static class ProductCart{
        private String name;
        private float prices;
        private String images;
        private String size;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public float getPrices() {
            return prices;
        }

        public void setPrices(float prices) {
            this.prices = prices;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public ProductCart(String name, String images, String size, float prices) {
            this.name = name;
            this.images = images;
            this.size = size;
            this.prices = prices;
        }

        public ProductCart() {
        }
    }
}
