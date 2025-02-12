package code.ecommerceproject.service;

import code.ecommerceproject.dto.CartProductDto;
import code.ecommerceproject.dto.CartRequestDto;
import code.ecommerceproject.entity.*;
import code.ecommerceproject.enums.OrderStatus;
import code.ecommerceproject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor()
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final StripeService stripeService;

    @Transactional
    public Order createOrder(final CartRequestDto cartRequestDto, final User loggedInUser) {
        final Order newOrder = new Order();
        final Set<OrderedProduct> orderedProducts = getOrderedProducts(cartRequestDto.getProductQuantity(), newOrder);

        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setOrderedProducts(orderedProducts);
        newOrder.setStripeSessionId(stripeService.createPayment(loggedInUser, orderedProducts));
        newOrder.setUser(loggedInUser);

        return orderRepository.save(newOrder);
    }

    private Set<OrderedProduct> getOrderedProducts(final Map<UUID, Integer> cartRequestDto, final Order order) {
        return cartRequestDto
                .entrySet()
                .stream()
                .map(entry -> {
                    final UUID productId = entry.getKey();
                    final Integer quantity = entry.getValue();
                    final Product product = productService.findById(productId).orElseThrow();

                    return OrderedProduct.builder()
                            .productId(productId)
                            .quantity(quantity)
                            .price(product.getPrice())
                            .productName(product.getName())
                            .order(order)
                            .build();
                }).collect(Collectors.toSet());

    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CartProductDto> getCartDetails(final CartRequestDto cartRequestDto) {

        final List<Product> products = getProducts(cartRequestDto);
        return mapToCartResponse(products, cartRequestDto.getProductQuantity());
    }

    private List<Product> getProducts(final CartRequestDto cartRequestDto) {
        final List<UUID> productIds = cartRequestDto
                .getProductQuantity()
                .keySet()
                .stream()
                .toList();

        return productService.findAllByIdIn(productIds);
    }

    private List<CartProductDto> mapToCartResponse(final List<Product> products,
                                                   final Map<UUID, Integer> productQuantity) {

        return products.stream()
                .map(product -> {
                    CartProductDto dto = new CartProductDto();
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setBrand(product.getProductBrand());
                    dto.setPictureUrl(
                            product.getPictures().stream()
                                    .filter(Picture::getFeatured)  // filter to find the featured picture
                                    .findFirst()  // get the first match (there should be only one featured picture)
                                    .map(Picture::getUrl)
                                    .orElse(null)
                    );
                    dto.setQuantity(productQuantity.get(product.getId()));
                    dto.setProductId(product.getId());
                    return dto;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOrder(final String stripeSessionId) {

        final Order order = orderRepository.findByStripeSessionId(stripeSessionId);
        order.setStatus(OrderStatus.PAID);

        productService.updateQuantity(order.getOrderedProducts());
    }
}