package code.ecommerceproject.controller;

import code.ecommerceproject.dto.CartProductDto;
import code.ecommerceproject.dto.CartRequestDto;
import code.ecommerceproject.dto.CartResponseDto;
import code.ecommerceproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/get-cart-details")
    public ResponseEntity<CartResponseDto> getDetails(@RequestBody CartRequestDto cartRequestDto) {
        final List<CartProductDto> cartProductDtos = orderService.getCartDetails(cartRequestDto);
        return ResponseEntity.ok(new CartResponseDto(cartProductDtos));
    }
} 