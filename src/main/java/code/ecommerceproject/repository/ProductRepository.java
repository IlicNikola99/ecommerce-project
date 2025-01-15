package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> {

    Product save(Product productToCreate);

    int deleteById(UUID id);

    Optional<Product> findById(UUID id);

    List<Product> findAllByIdIn(List<UUID> ids);
}
