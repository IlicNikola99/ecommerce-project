package code.ecommerceproject.dto;

import code.ecommerceproject.entity.OrderedProduct;
import code.ecommerceproject.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private UUID id;
    private OrderStatus status;
    private String stripeSessionId;
    private Set<OrderedProduct> orderedProducts = new HashSet<>();
    private UserDto user;
}
