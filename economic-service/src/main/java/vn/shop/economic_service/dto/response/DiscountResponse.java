package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountResponse {
    String id;
    String code;
    LocalDate start_at;
    LocalDate end_at;
    boolean active;
}
