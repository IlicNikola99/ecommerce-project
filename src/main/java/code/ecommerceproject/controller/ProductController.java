package code.ecommerceproject.controller;

import code.ecommerceproject.dto.ProductDto;
import code.ecommerceproject.entity.Product;
import code.ecommerceproject.mapper.ProductMapper;
import code.ecommerceproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ProductDto> save(@RequestBody final ProductDto productDto) {

        final Product productEntity = ProductMapper.Instance.toEntity(productDto);
        productEntity.getPictures().forEach(pictureEntity -> {
            pictureEntity.setProduct(productEntity);//TODO move this to mapper
        });
        final Product savedProduct = productService.save(productEntity);
        return ResponseEntity.ok(ProductMapper.Instance.toDto(savedProduct));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UUID> delete(final @RequestParam("productId") UUID id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            log.error("Could not delete product with id {}", id, e);
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ProductDto>> getAll(final Pageable pageable) {
        final Page<Product> products = productService.findAll(pageable);

        final Page<ProductDto> restProducts = new PageImpl<>(
                products.getContent().parallelStream().map(ProductMapper.Instance::toDto).collect(Collectors.toList()),
                pageable,
                products.getTotalElements()
        );
        return ResponseEntity.ok(restProducts);
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<ProductDto>> getAllFeatured(final Pageable pageable) {
        final Page<Product> products = productService.findAllFeatured(pageable);

        final Page<ProductDto> restProducts = new PageImpl<>(
                products.getContent().parallelStream().map(ProductMapper.Instance::toDto).collect(Collectors.toList()),
                pageable,
                products.getTotalElements()
        );
        return ResponseEntity.ok(restProducts);
    }
}