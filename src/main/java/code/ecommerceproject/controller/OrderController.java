package code.ecommerceproject.controller;

import code.ecommerceproject.dto.CartItemsDto;
import code.ecommerceproject.dto.CartRequestDto;
import code.ecommerceproject.dto.CartResponseDto;
import code.ecommerceproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get-cart-details")
    public ResponseEntity<List<CartResponseDto>> getDetails(@RequestParam List<UUID> productIds) {
        final List<CartItemsDto> cartItemsDtos = productIds.stream()
                .map(uuid -> new CartItemsDto(uuid, 1))
                .toList();

        final CartRequestDto cartRequestDto = CartRequestDto.builder().cartItems(cartItemsDtos).build();
        final List<CartResponseDto> cartResponseDtos = orderService.getCartDetails(cartRequestDto);
        return ResponseEntity.ok(cartResponseDtos);
    }
} 