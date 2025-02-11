package code.ecommerceproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "ordered_product")
@Data
public class OrderedProduct {

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Column(name = "product_name", nullable = false)
    private String productName;
}