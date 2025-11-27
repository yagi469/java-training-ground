package com.example.week6.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.example.week6.blog.dto.CategoryRequest;
import com.example.week6.blog.dto.CategoryResponse;
import com.example.week6.blog.entity.Category;
import com.example.week6.blog.exception.CategoryNotFoundException;
import com.example.week6.blog.repository.CategoryRepository;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id)));
    }
    
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return toResponse(savedCategory);
    }
    
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        existingCategory.setName(request.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return toResponse(updatedCategory);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    
    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName()
        );
    }
    
    private Category toEntity(CategoryRequest request) {
        return new Category(
            null,
            request.getName(),
            new ArrayList<>()
        );
    }
}
