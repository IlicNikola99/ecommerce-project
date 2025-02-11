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

    @Query("SELECT p FROM Product p WHERE p.id != :productId AND p.category.id = :categoryId")
    Page<Product> findRelatedProducts(@Param("productId") UUID productId, @Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:sizes IS NULL OR p.size IN :sizes) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findByCategoryAndSizes(@Param("categoryId") UUID categoryId, @Param("sizes") List<Double> sizes, Pageable pageable);

}
