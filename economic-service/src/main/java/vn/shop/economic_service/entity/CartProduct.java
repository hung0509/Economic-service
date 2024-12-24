package vn.shop.economic_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
//import vn.shop.economic_service.entity.key.CartProductKey;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    Cart cart_pr;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    Product product_pr;

    int amount;
    String size;
}
