package code.ecommerceproject.controller;

import code.ecommerceproject.dto.CartProductDto;
import code.ecommerceproject.dto.CartRequestDto;
import code.ecommerceproject.dto.CartResponseDto;
import code.ecommerceproject.dto.StripeSessionIdDto;
import code.ecommerceproject.entity.Order;
import code.ecommerceproject.entity.User;
import code.ecommerceproject.service.OrderService;
import code.ecommerceproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/get-cart-details")
    public ResponseEntity<CartResponseDto> getDetails(@RequestBody CartRequestDto cartRequestDto) {
        final List<CartProductDto> cartProductDtos = orderService.getCartDetails(cartRequestDto);
        return ResponseEntity.ok(new CartResponseDto(cartProductDtos));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/create-order")
    public ResponseEntity<StripeSessionIdDto> createOrder(@RequestBody CartRequestDto cartRequestDto, @RequestParam String userEmail) {

        final User loggedInUser = userService.findUserByEmail(userEmail);
        final Order createdOrder = orderService.createOrder(cartRequestDto, loggedInUser);
        return ResponseEntity.ok(new StripeSessionIdDto(createdOrder.getStripeSessionId()));

    }
} 