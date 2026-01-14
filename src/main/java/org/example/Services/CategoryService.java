package org.example.Services;

import org.example.Entities.Category;
import java.util.List;

public interface CategoryService {
    Category getCategoryByName(String name);
    Category createCategory(String name);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    void seedDefaultCategories();

}
