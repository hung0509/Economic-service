package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Category;
import vn.shop.economic_service.entity.Discount;
import vn.shop.economic_service.entity.Image;

import java.time.LocalDate;
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
    int stock_quantity;
    Discount discount;
    String category;
    Set<Image> images;
}
