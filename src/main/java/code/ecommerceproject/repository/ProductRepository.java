package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Product save(Product productToCreate);

    int delete(Long id);

    Optional<Product> findById(Long id);

    List<Product> findAllByIdIn(List<Long> ids);
}
