package org.example.post;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repositories.PostRepositoryImpl;
import org.example.Services.PostService;
import org.example.Services.PostServiceImpl;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.LINUX)
public class PostServiceTest {

    private EntityManagerFactory emf;
    private PostService postService;

    @BeforeEach
    public void setup() {

        var cfg =
            new HibernatePersistenceConfiguration("emf")
                .jdbcUrl("jdbc:mysql://localhost:3306/bulletin_test")
                .jdbcUsername("root")
                .jdbcPassword("root")
                .property("hibernate.hbm2ddl.auto", "update")
                .managedClasses(User.class, Post.class, Category.class);

        emf = cfg.createEntityManagerFactory();
        var postRepo = new PostRepositoryImpl(emf);
        postService = new PostServiceImpl(postRepo);
    }
    @AfterEach
    public void tearDown() {

    }


}
