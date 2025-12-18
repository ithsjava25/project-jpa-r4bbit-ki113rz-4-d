package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.Entities.Post;

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
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            if (post.getPostId() == 0) {
                em.persist(post);
            } else {
                post = em.merge(post);
            }

            tx.commit();
            return post;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Post> findById(int postId) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Post.class, postId));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Post> findAll() {
        EntityManager em = emf.createEntityManager();
            try {
                return em.createQuery(
                    "select p from Post p order by p.createdAt desc",
                    Post.class
                ).getResultList();
            } finally {
                em.close();
            }
    }

    @Override
    public void deleteById(int postId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Post post = em.find(Post.class, postId);
            if (post != null){
                em.remove(post);
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
