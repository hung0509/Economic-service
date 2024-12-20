package vn.shop.economic_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    int quantity;

    @ManyToOne()
    @JoinColumn(name="product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
