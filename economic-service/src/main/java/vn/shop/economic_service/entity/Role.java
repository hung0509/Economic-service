package vn.shop.economic_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    String name;

    String description;

    @ManyToMany()
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name= "name_role"),
            inverseJoinColumns = @JoinColumn(name = "name_permission")
    )
    @JsonManagedReference
    Set<Permission> permissions;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    Set<User> users;
}
