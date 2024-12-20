package vn.shop.economic_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String code;
    LocalDate start_at;
    LocalDate end_at;
    boolean active;

    @OneToOne(mappedBy = "discount")
    @JsonBackReference
    Product product;
}
