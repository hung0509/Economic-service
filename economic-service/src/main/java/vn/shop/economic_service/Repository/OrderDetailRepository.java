package vn.shop.economic_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.shop.economic_service.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
