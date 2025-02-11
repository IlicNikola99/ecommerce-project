package code.ecommerceproject.service;

import code.ecommerceproject.entity.Product;
import code.ecommerceproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product save(final Product newProduct) {
        return productRepository.save(newProduct);
    }

    public Page<Product> findAll(final Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public void delete(final UUID id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> findById(final UUID id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllByIdIn(final List<UUID> ids) {
        return productRepository.findAllByIdIn(ids);
    }

    public Page<Product> findAllFeatured(final Pageable pageable) {
        return productRepository.findAllFeatured(pageable);
    }

    public Page<Product> findRelatedProducts(final UUID productId, final Pageable pageable) {
        final Product product = productRepository.findById(productId).orElseThrow();
        return productRepository.findRelatedProducts(productId, product.getCategory().getId(), pageable);
    }

    public Page<Product> findByCategoryAndSizes(final UUID categoryId, final List<Double> sizes, final Pageable pageable) {
        return productRepository.findByCategoryAndSizes(categoryId, sizes, pageable);
    }
}
