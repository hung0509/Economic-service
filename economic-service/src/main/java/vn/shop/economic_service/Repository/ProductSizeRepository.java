package vn.shop.economic_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shop.economic_service.entity.ProductSize;
import vn.shop.economic_service.entity.key.ProductSizeKey;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, ProductSizeKey> {
}
