package org.example.Services;

import org.example.Entities.Category;
import org.example.Repositories.CategoryRepository;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Category createCategory(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null");
        }
        String trimmed = name.trim();
        return categoryRepo.findByName(trimmed)
            .orElseGet(() -> categoryRepo.save(new Category(trimmed)));
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        if (id == null) throw new IllegalArgumentException("Category id cannot be null");
        return categoryRepo.getById(id);
    }

    @Override
    public Category getCategoryByName (String name) {
        return categoryRepo.findByName(name).orElse(null);
    }

    @Override
    public void seedDefaultCategories() {
        createCategory("Skola");
        createCategory("Job");
        createCategory("Privat");
        createCategory("Viktigt");
        createCategory("Övrigt");
    }
}
