package code.ecommerceproject.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CartProductDto {
    private String name;
    private Double price;
    private String brand;
    private String pictureUrl;
    private int quantity;
    private UUID productId;
} 