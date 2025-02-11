package code.ecommerceproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = "pictures")
@Entity
@Table(name = "product")
@Data
public class Product extends AbstractAuditingEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "color")
    private String color;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "size")
    private Double size;

    @Column(name = "featured")
    private boolean featured;

    @Column(name = "nb_in_stock")
    private int nbInStock;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Picture> pictures = new HashSet<>();
}
