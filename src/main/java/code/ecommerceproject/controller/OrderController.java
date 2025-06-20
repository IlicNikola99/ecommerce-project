package code.ecommerceproject.controller;

import code.ecommerceproject.dto.*;
import code.ecommerceproject.entity.Order;
import code.ecommerceproject.entity.User;
import code.ecommerceproject.mapper.OrderMapper;
import code.ecommerceproject.service.AddressService;
import code.ecommerceproject.service.OrderService;
import code.ecommerceproject.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Address;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final UserService userService;
    private final AddressService addressService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/all-orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {

        final List<Order> orderEntities = orderService.getAllOrders();
        return ResponseEntity.ok(OrderMapper.Instance.toDtoList(orderEntities));

    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhookStripe(@RequestBody String paymentEvent,
                                              @RequestHeader("Stripe-Signature") String stripeSignature) {
        Event event;
        try {
            event = Webhook.constructEvent(
                    paymentEvent, stripeSignature, webhookSecret
            );
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }

        final Optional<StripeObject> rawStripeObjectOpt = event.getDataObjectDeserializer().getObject();

        if (event.getType().equals("checkout.session.completed")) {
            handleCheckoutSessionCompleted(rawStripeObjectOpt.orElseThrow());
        }

        return ResponseEntity.ok().build();
    }

    private void handleCheckoutSessionCompleted(StripeObject rawStripeObject) {
        if (rawStripeObject instanceof Session session) {
            final Address address = session.getCustomerDetails().getAddress();
            final User user = userService.findUserByEmail(session.getCustomerDetails().getEmail());
            code.ecommerceproject.entity.Address userAddress = code.ecommerceproject.entity.Address.builder()
                    .city(address.getCity())
                    .country(address.getCountry())
                    .zipCode(address.getPostalCode())
                    .street(address.getLine1())
                    .user(user)
                    .build();

            final code.ecommerceproject.entity.Address savedAddress = addressService.createAddress(userAddress);

            log.info("Saved address: {}", savedAddress);

            final String stripeSessionId = session.getId();

            orderService.updateOrder(stripeSessionId);
        }
    }
} 