package code.ecommerceproject.service;

import code.ecommerceproject.dto.CartItemsDto;
import code.ecommerceproject.dto.CartRequestDto;
import code.ecommerceproject.dto.CartResponseDto;
import code.ecommerceproject.entity.Order;
import code.ecommerceproject.entity.Product;
import code.ecommerceproject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor()
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
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
    public List<CartResponseDto> getCartDetails(final CartRequestDto cartRequestDto) {

        final List<UUID> productIds = cartRequestDto.getCartItems().stream()
                .map(CartItemsDto::getProductId)
                .toList();

        final List<Product> products = productService.findAllByIdIn(productIds);

        return mapToCartResponse(products);

    }

    private List<CartResponseDto> mapToCartResponse(final List<Product> products) {
        final Map<UUID, Long> productCount = products.stream()
                .collect(Collectors.groupingBy(Product::getId, Collectors.counting()));

        return products.stream()
                .map(product -> {
                    CartResponseDto dto = new CartResponseDto();
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setBrand(product.getProductBrand());
                    dto.setPictureUrl(product.getPictures().isEmpty() ? null : product.getPictures().stream().reduce((first, second) -> second).get().getUrl());
                    dto.setQuantity(productCount.get(product.getId()).intValue());
                    dto.setProductId(product.getId());
                    return dto;
                })
                .distinct()
                .collect(Collectors.toList());
    }
} 