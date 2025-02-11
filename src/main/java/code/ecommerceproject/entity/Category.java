package code.ecommerceproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = "products")
@Entity
@Table(name = "category")
@Data
@ToString
public class Category extends AbstractAuditingEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
