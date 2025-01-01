package vn.shop.economic_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    DeliveryMethod deliveryMethod;

    @ManyToOne()
    @JoinColumn(name="payment_id")
    @JsonManagedReference
    PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<OrderDetail> orderDetails;

    @ManyToOne()
    @JoinColumn(name="user_id")
    @JsonManagedReference
    User user;
}
