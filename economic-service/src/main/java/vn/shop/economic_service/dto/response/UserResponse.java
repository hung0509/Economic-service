package vn.shop.economic_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.shop.economic_service.entity.Andress;
import vn.shop.economic_service.entity.Order;
import vn.shop.economic_service.entity.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password;
    String firstname;
    String lastname;
    String email;
    String phone;
    Set<Role> roles;
    Andress andress;
    Set<Order> orders;
    String cart_id;
}
