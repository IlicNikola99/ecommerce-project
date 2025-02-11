package code.ecommerceproject.entity;

import code.ecommerceproject.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order")
@Data
public class Order extends AbstractAuditingEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "stripe_session_id", nullable = false)
    private String stripeSessionId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<OrderedProduct> orderedProducts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public static Order create(final User connectedUser, final String stripeSessionId, final Set<OrderedProduct> products) {
        final Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(connectedUser);
        order.setStripeSessionId(stripeSessionId);
        order.setStatus(OrderStatus.PAID);
        order.setOrderedProducts(products);

        return order;
    }

}
