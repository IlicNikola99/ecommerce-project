package code.ecommerceproject.controller;

import code.ecommerceproject.dto.PictureDto;
import code.ecommerceproject.dto.ProductDto;
import code.ecommerceproject.entity.Product;
import code.ecommerceproject.exception.MultipartPictureException;
import code.ecommerceproject.mapper.ProductMapper;
import code.ecommerceproject.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> save(final MultipartHttpServletRequest request,
                                           @RequestPart("dto") final String productRaw) throws JsonProcessingException {

        final List<PictureDto> pictures = request.getFileMap()
                .values()
                .stream()
                .map(mapMultipartFileToPicture())
                .toList();

        final ProductDto productDto = objectMapper.readValue(productRaw, ProductDto.class);
        productDto.setPictures(pictures);
        final Product productEntity = ProductMapper.Instance.toEntity(productDto);
        final Product savedProduct = productService.save(productEntity);
        return ResponseEntity.ok(ProductMapper.Instance.toDto(savedProduct));
    }


    private Function<MultipartFile, PictureDto> mapMultipartFileToPicture() {
        return multipartFile -> {
            try {
                final PictureDto dto = new PictureDto();
                dto.setFile(multipartFile.getBytes());
                dto.setMimeType(multipartFile.getContentType());
                return dto;
            } catch (IOException ieo) {
                throw new MultipartPictureException(String.format("Cannot parse multipart file : %s", multipartFile.getOriginalFilename()));
            }
        };
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
                products.getContent().stream().map(ProductMapper.Instance::toDto).collect(Collectors.toList()),
                pageable,
                products.getTotalElements()
        );
        return ResponseEntity.ok(restProducts);
    }
}