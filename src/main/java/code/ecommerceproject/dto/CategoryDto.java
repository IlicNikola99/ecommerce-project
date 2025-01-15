package code.ecommerceproject.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CategoryDto {

    private UUID id;
    private String name;
    private Set<ProductDto> products;
}