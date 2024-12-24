package vn.shop.economic_service.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shop.economic_service.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, String> {
}
