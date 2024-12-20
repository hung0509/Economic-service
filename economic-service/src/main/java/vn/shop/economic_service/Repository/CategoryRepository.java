package vn.shop.economic_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.shop.economic_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
