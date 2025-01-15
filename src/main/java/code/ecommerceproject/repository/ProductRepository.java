package code.ecommerceproject.repository;

import code.ecommerceproject.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<User, Long> {
}
