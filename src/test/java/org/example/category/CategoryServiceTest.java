package org.example.category;



import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.Repositories.CategoryRepositoryImpl;
import org.example.Services.CategoryService;
import org.example.Services.CategoryServiceImpl;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.JPAUtil.emf;

@DisabledOnOs(OS.LINUX)
public class CategoryServiceTest {

    private EntityManagerFactory emf;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp()
    {
        var cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/bulletin_test")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto", "update")
            .managedClasses(Category.class, Post.class, User.class, Profile.class);

        emf = cfg.createEntityManagerFactory();
        categoryService = new CategoryServiceImpl(new CategoryRepositoryImpl(emf));

    }

    @AfterEach
    public void tearDown()
    {
        emf.close();
    }

    @Test
    public void createCategory() {
        Category category = categoryService.createCategory("General");

        assertThat(category).isNotNull();
        assertThat(category.getCategoryId()).isNotNull();

    }

    @Test
    public void findCategoryById() {
        Category category = categoryService.createCategory("General");
        Category id = categoryService.getCategoryById(category.getCategoryId());


        assertThat(id.getCategoryId()).isEqualTo(category.getCategoryId());

    }

    @Test
    public void findAllCategories() {

    }

}
