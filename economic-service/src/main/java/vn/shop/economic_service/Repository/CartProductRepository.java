package vn.shop.economic_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.shop.economic_service.entity.CartProduct;

import java.util.Optional;
//import vn.shop.economic_service.entity.key.CartProductKey;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    @Query("SELECT cp FROM CartProduct cp JOIN cp.product_pr p JOIN cp.cart_pr c WHERE p.id = :pro_id AND c.id = :cart_id AND cp.size = :size")
    CartProduct findByProductByCart(@Param("pro_id") String proId, @Param("cart_id") String cartId, @Param("size") String size);
}
