package vn.shop.economic_service.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@Getter
@Setter
public class ProductSizeKey implements Serializable {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "size_id")
    private String sizeId;

    public ProductSizeKey(String id, String name) {
        this.productId = id;
        this.sizeId = name;
    }

    public ProductSizeKey(){

    }
    // Constructors, Getters, Setters, hashCode, and equals
}
