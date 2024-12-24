package vn.shop.economic_service.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.shop.economic_service.entity.Category;
import vn.shop.economic_service.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = "SELECT p FROM Product p JOIN p.discount d WHERE d.active = true ORDER BY p.updated_at DESC")
    List<Product> fidAllByDiscount(Pageable pageable);
    List<Product> findAllByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN p.sizes s WHERE p.id = :id and s.size.name = :size")
    Product findByIdAndSize(String id, String size);
}
