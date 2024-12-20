package vn.shop.economic_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;
    String vendor;
    float price;
    int stock_quantity;
    LocalDate created_at;
    LocalDate updated_at;

    @OneToMany(mappedBy = "product_pr")
    @JsonBackReference
    Set<CartProduct> carts;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    @JsonBackReference
    Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="discount_id")
    @JsonManagedReference
    Discount discount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Image> images;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    Set<OrderDetail> order_details;
}
