package org.example.post;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repositories.CategoryRepositoryImpl;
import org.example.Repositories.UserRepositoryImpl;
import org.example.Services.PostService;
import org.example.Services.PostServiceImpl;
import org.example.Services.UserService;
import org.example.Services.UserServiceImpl;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.example.Repositories.PostRepository;
import org.example.Repositories.PostRepositoryImpl;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//==========//==========//
/*
 * These tests verify the business rules and expected behavior
 * for posts within the application.
 *
 *
 *                      Post rules
 *
 *     Post must have subject
 *     Post must have message
 *     Posts are ordered by creation time (newest first)
 *     A post is associated with its author
 *
 */
//==========//==========//


@DisabledOnOs(OS.LINUX)
public class PostServiceTest {

    private EntityManagerFactory emf;
    private PostService postService;
    private UserService userService;

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
        var userRepo = new UserRepositoryImpl(emf);
        var categoryRepo = new CategoryRepositoryImpl(emf);
        postService = new PostServiceImpl(postRepo, categoryRepo);

        emf.runInTransaction(em ->
            em.persist(new Category("General")));




    }
    @AfterEach
    public void tearDown() {

    }

    @Test
    void shouldNotCreatePostWhenSubjectIsBlank() {
        User user = new User("Fiffen", "Friberg", "fiffen", "secret123");

        assertThatThrownBy(() ->
            postService.createPost(
                "",
                "Fiffen is king",
                List.of(1L),
                user
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Subject");
    }


    //todo: add , "shouldNotCreatePostWhenMessageIsBlank" , "shouldCreatePostWhenInputIsValid", "shouldDeletePost", "shouldUpdatePost",





}
