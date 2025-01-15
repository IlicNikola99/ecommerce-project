package code.ecommerceproject.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Long id;
    private String productBrand;
    private String color;
    private String description;
    private String name;
    private double price;
    private String size;
    private boolean featured;
    private int nbInStock;
    private CategoryDto category;
}