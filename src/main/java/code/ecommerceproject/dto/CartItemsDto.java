package code.ecommerceproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CartItemsDto {
    private UUID productId;
    private int quantity;
} 