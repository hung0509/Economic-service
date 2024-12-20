package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Product;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String name;
    int quantity;
}
