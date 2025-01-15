package code.ecommerceproject.entity;

import code.ecommerceproject.enums.ProductSize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Data
public class Product extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
    @Enumerated(EnumType.STRING)
    private ProductSize size;

    @Column(name = "featured")
    private boolean featured;

    @Column(name = "nb_in_stock")
    private int nbInStock;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    //private List<Picture> pictures;
}
