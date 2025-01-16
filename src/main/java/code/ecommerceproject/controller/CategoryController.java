package code.ecommerceproject.controller;

import code.ecommerceproject.dto.CategoryDto;
import code.ecommerceproject.entity.Category;
import code.ecommerceproject.mapper.CategoryMapper;
import code.ecommerceproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryDto> save(final @RequestBody CategoryDto categoryDto) {
        final Category categoryEntity = CategoryMapper.Instance.toEntity(categoryDto);
        final Category categorySaved = categoryService.save(categoryEntity);
        return ResponseEntity.ok(CategoryMapper.Instance.toDto(categorySaved));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UUID> delete(final UUID categoryId) {
        try {
            categoryService.delete(categoryId);
            return ResponseEntity.ok(categoryId);
        } catch (Exception e) {
            log.error("Could not delete category with id {}", categoryId, e);
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> findAll(final Pageable pageable) {
        final Page<Category> categories = categoryService.findAll(pageable);
        final PageImpl<CategoryDto> restCategories = new PageImpl<>(
                categories.getContent().stream().map(CategoryMapper.Instance::toDto).toList(),
                pageable,
                categories.getTotalElements()
        );
        return ResponseEntity.ok(restCategories);
    }

}