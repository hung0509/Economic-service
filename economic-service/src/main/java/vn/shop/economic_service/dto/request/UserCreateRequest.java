package vn.shop.economic_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Andress;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String username;
    String password;
    String firstname;
    String lastname;
    String email;
    String phone;
}
