package org.example.Repositories;

import org.example.Entities.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional <Category> getById(Long id);
    List<Category> findAll();
    Optional<Category> findByName(String name);
    Category save(Category category);
}
