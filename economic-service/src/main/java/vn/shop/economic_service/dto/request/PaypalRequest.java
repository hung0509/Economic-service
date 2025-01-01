package vn.shop.economic_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaypalRequest {
    float total;
    String description;
    String urlError;
    String urlSuccess;
    String currency;
    String intent;
    String method;
}
