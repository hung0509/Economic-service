package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Discount;
import vn.shop.economic_service.entity.ProductSize;
import vn.shop.economic_service.entity.Size;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String description;
    String vendor;
    float price;
    Set<ProductSize> sizes;
    Discount discount;
    String category;
    String images;
}
