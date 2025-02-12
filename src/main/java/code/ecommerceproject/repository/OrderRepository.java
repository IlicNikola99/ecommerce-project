package code.ecommerceproject.repository;

import code.ecommerceproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Order findByStripeSessionId(final String stripeSessionId);
} 