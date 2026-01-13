package org.example.post;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.Repositories.CategoryRepositoryImpl;
import org.example.Repositories.UserRepositoryImpl;
import org.example.Services.*;
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
import static org.assertj.core.api.Assertions.assertThat;


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
    private Category testCategory;
    private User testUser;

    @BeforeEach
    public void setup() {

        var cfg =
            new HibernatePersistenceConfiguration("emf")
                .jdbcUrl("jdbc:mysql://localhost:3306/bulletin_test")
                .jdbcUsername("root")
                .jdbcPassword("root")
                .property("hibernate.hbm2ddl.auto", "update")
                .managedClasses(User.class, Post.class, Category.class, Profile.class);

        emf = cfg.createEntityManagerFactory();
        var postRepo = new PostRepositoryImpl(emf);
        var userRepo = new UserRepositoryImpl(emf);
        var categoryRepo = new CategoryRepositoryImpl(emf);

        postService = new PostServiceImpl(postRepo, categoryRepo);
        userService = new UserServiceImpl(userRepo);

        emf.runInTransaction(em -> {
            testCategory = new Category("General");
            em.persist(testCategory);

            testUser = new User("Fiffen", "Friberg", "fiffen", "secret123");
            em.persist(testUser);
        });

    }
    @AfterEach
    public void tearDown() {
        if (emf != null) {
            emf.runInTransaction(em -> {
                em.createQuery("DELETE FROM Post").executeUpdate();
                em.createQuery("DELETE FROM Category").executeUpdate();
                em.createQuery("DELETE FROM User").executeUpdate();
            });
            emf.close();
        }
    }

    @Test
    void shouldNotCreatePostWhenSubjectIsBlank() {

        assertThatThrownBy(() ->
            postService.createPost(
                "",
                "Fiffen is king",
                List.of(1L),
                testUser
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Subject");
    }

    @Test
    void shouldNotCreatePostWhenMessageIsBlank() {
        assertThatThrownBy(() ->
            postService.createPost(
                "This post is so empty i can't see it",
                "",
                List.of(testCategory.getCategoryId()),
                testUser
            )
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Message");
    }

    @Test
    void shouldCreatePostWhenInputIsValid() {
        Post post = postService.createPost(
            "Hello",
            "This is a post",
            List.of(testCategory.getCategoryId()),
            testUser
        );

        assertThat(post).isNotNull();
        assertThat(post.getPostId()).isNotNull();
        assertThat(post.getAuthor()).isEqualTo(testUser);
        assertThat(post.getCategories()).hasSize(1);
        assertThat(post.getPostItColor()).isNotNull();
    }

    @Test
    void shouldUpdatePost() {
        Post post = postService.createPost(
            "Old subject",
            "Old message",
            List.of(testCategory.getCategoryId()),
            testUser
        );

        postService.updatePost(
            post,
            "New subject",
            "New message",
            List.of(testCategory.getCategoryId())
        );

        Optional<Post> updated =
            ((PostServiceImpl) postService).getPostById(Long.valueOf(post.getPostId()));

        assertThat(updated).isPresent();
        assertThat(updated.get().getSubject()).isEqualTo("New subject");
        assertThat(updated.get().getMessage()).isEqualTo("New message");
    }

    @Test
    void shouldDeletePost() {
        Post post = postService.createPost(
            "Where did I put my glasses?",
            "Will delete this post when found",
            List.of(testCategory.getCategoryId()),
            testUser
        );

        postService.deletePost(post);

        Optional<Post> deleted =
            ((PostServiceImpl) postService).getPostById(Long.valueOf(post.getPostId()));


        assertThat(deleted).isEmpty();
    }

}
