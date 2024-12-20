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
public class Andress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String apartment_no;
    String sweet_name;
    String city;

    LocalDate created_at;
    LocalDate updated_at;

    @OneToOne(mappedBy = "andress", fetch = FetchType.EAGER)
    @JsonBackReference
    User user;
}
