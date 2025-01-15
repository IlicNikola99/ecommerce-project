package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Category save(Category category);

    int delete(Long categoryId);
}
