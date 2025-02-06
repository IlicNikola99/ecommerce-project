package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findById(UUID id);

    List<Product> findAllByIdIn(List<UUID> ids);

    @Query("SELECT p FROM Product p WHERE p.featured = true")
    Page<Product> findAllFeatured(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :productId")
    Page<Product> findRelatedInCategory(@Param("categoryId") UUID categoryId, @Param("productId") UUID excludedProductId, Pageable pageable);

}
