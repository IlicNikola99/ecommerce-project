package code.ecommerceproject.service;

import code.ecommerceproject.dto.CartProductDto;
import code.ecommerceproject.dto.CartRequestDto;
import code.ecommerceproject.entity.Order;
import code.ecommerceproject.entity.Picture;
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
    public List<CartProductDto> getCartDetails(final CartRequestDto cartRequestDto) {

        final List<UUID> productIds = cartRequestDto
                .getProductQuantity()
                .keySet()
                .stream()
                .toList();

        final List<Product> products = productService.findAllByIdIn(productIds);

        return mapToCartResponse(products, cartRequestDto.getProductQuantity());

    }

    private List<CartProductDto> mapToCartResponse(final List<Product> products, final Map<UUID, Integer> productQuantity) {

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
} 