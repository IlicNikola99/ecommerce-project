package code.ecommerceproject.service;

import code.ecommerceproject.entity.Category;
import code.ecommerceproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    public Category findByName(final String name) {
        return categoryRepository.findByName(name).orElseThrow();
    }


    @Transactional
    public void delete(final UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}