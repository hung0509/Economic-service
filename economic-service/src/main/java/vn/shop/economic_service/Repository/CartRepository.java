package vn.shop.economic_service.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.shop.economic_service.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, String> {
}
