package vn.shop.economic_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="[order]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String andress;
    float total_money;
    String status_payment;
    String status_delivery;
    LocalDate order_date;

    @ManyToOne()
    @JoinColumn(name="delivery_id")
    DeliveryMethod deliveryMethod;

    @ManyToOne()
    @JoinColumn(name="payment_id")
    PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    Set<OrderDetail> orderDetails;

    @ManyToOne()
    @JoinColumn(name="user_id")
    User user;
}
