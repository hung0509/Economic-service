package vn.shop.economic_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shop.economic_service.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
