package org.example;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Category getById(Long id);
    List<Category> findAll();
    Optional<Category> findByName(String name);
}
