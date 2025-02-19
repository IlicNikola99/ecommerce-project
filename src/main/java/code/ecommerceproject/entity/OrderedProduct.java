package code.ecommerceproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ordered_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProduct extends AbstractAuditingEntity<UUID> {

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Override
    public UUID getId() {
        return productId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderedProduct that = (OrderedProduct) o;
        return Objects.equals(productId, that.productId) && Objects.equals(order.getId(), that.order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, order);
    }
}