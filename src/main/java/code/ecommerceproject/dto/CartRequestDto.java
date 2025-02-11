package code.ecommerceproject.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartRequestDto {
    private List<CartItemsDto> cartItems;
} 