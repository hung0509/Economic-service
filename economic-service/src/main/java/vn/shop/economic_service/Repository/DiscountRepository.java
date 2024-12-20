package vn.shop.economic_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shop.economic_service.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    Discount findByCode(String name);
}
