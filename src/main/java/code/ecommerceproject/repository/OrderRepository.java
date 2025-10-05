package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Order findByStripeSessionId(final String stripeSessionId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserId(final UUID userId);
} 