package code.ecommerceproject.service;

import code.ecommerceproject.entity.Category;
import code.ecommerceproject.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category save(final Category categoryDto) {
        final Category category = new Category();
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    public Page<Category> findAll(final Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Transactional
    public Long delete(final Long categoryId) {
        int nbOfRowsDeleted = categoryRepository.delete(categoryId);
        if (nbOfRowsDeleted != 1) {
            throw new EntityNotFoundException(String.format("No category deleted with id %s", categoryId));
        }
        return categoryId;
    }
}