package org.example.Repositories;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.EntityManagerFactoryWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles post data from database
 */
public class PostRepositoryImpl implements PostRepository {

    private final EntityManagerFactory emf;

    public PostRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Implement methods here:

    @Override
    public Post save(Post post) {
        return EntityManagerFactoryWrapper.callInTransaction(emf, em -> {

            if (post.getPostId() == 0) {
                em.persist(post);
                return post;
            } else {
                return em.merge(post);
            }
        });
    }


    @Override
    public Post updatePost(Post post) {
        return EntityManagerFactoryWrapper.callInTransaction(emf, em -> {
            Post existing = em.find(Post.class, post.getPostId());
            if (existing == null) {
                throw new IllegalArgumentException("Post not found: " + post.getPostId());
            }
            existing.setSubject(post.getSubject());
            existing.setMessage(post.getMessage());

            List<Category> managedCategories = new ArrayList<>();
            for (Category category : post.getCategories()) {
                Category managedCategory = em.find(Category.class, category.getCategoryId());

                if (managedCategory == null) {
                    throw new IllegalArgumentException("Category " + category.getCategoryId() + " does not exist");
                }
                managedCategories.add(managedCategory);
            }
            existing.setCategories(managedCategories);
            return existing; // Uppdaterad post
        });
    }

    @Override
    public List<Post> getPostsByUser(User user) {
        return emf.callInTransaction(em ->
                em.createQuery(
                        "select DISTINCT p from Post p " +
                            "JOIN FETCH p.author a " +
                            "LEFT JOIN FETCH a.profile " + // Hämtar profilen direkt
                            "LEFT JOIN FETCH p.categories " + // Hämtar kategorierna direkt
                            "where p.author = :user " +
                            "order by p.createdAt desc",
                        Post.class
                    )
                    .setParameter("user", user)
            .getResultList()
            );
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return EntityManagerFactoryWrapper.callInTransaction(emf, em ->
            Optional.ofNullable(em.find(Post.class, id))
        );
    }

    @Override
    public List<Post> findAll() {
        return EntityManagerFactoryWrapper.callInTransaction(emf, em ->
            em.createQuery("""
                    SELECT DISTINCT p
                    FROM Post p
                    LEFT JOIN FETCH p.author a
                    LEFT JOIN FETCH a.profile
                    LEFT JOIN FETCH p.categories
                    ORDER BY p.createdAt DESC
                    """ , Post.class)
                .getResultList()
        );
    }
    @Override
    public Post findByIdWithAuthorAndCategories(long id) {
        return EntityManagerFactoryWrapper.callInTransaction(emf, em ->
            em.createQuery("""
            select p
            FROM Post p
            LEFT JOIN FETCH p.author
            LEFT JOIN FETCH p.categories
            LEFT JOIN FETCH p.updatedBy
            WHERE p.postId = :id
        """, Post.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null));
    }

    @Override
    public void deleteById(int postId) {
        EntityManagerFactoryWrapper.callInTransaction(emf,em -> {
            Post post = em.find(Post.class, postId);
            if (post != null) {
                em.remove(post);
            }
            return null;
        });
    }
}
