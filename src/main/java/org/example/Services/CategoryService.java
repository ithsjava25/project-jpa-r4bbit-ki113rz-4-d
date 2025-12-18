package org.example;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name);
    List<Category> getAllCategories();
    Category getCategoryByid(Long id);
    void seedDefaultCategories();
}
