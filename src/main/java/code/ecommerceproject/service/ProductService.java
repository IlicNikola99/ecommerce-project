package code.ecommerceproject.service;

import code.ecommerceproject.dto.ProductDto;
import code.ecommerceproject.entity.Product;
import code.ecommerceproject.mapper.ProductMapper;
import code.ecommerceproject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product save(final ProductDto newProduct) {

        final Product productEntity = ProductMapper.Instance.toEntity(newProduct);
        return productRepository.save(productEntity);
    }

    public Page<Product> findAll(final Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public Long delete(final Long id) {
        int nbOfRowsDeleted = productRepository.delete(id);
        if (nbOfRowsDeleted != 1) {
            throw new EntityNotFoundException(String.format("No Product deleted with id %s", id));
        }
        return id;
    }

    public Optional<Product> findById(final Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllByIdIn(List<Long> ids) {
        return productRepository.findAllByIdIn(ids);
    }
}
