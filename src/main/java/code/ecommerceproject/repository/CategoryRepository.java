package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, UUID> {

    Optional<Category> findByName(String name);

    Category save(Category category);

    int deleteById(UUID id);
}
