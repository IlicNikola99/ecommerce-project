package code.ecommerceproject.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDto {

    private UUID id;
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