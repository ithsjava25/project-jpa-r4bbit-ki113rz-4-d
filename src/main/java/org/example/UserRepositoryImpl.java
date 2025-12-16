package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.*;
import org.hibernate.sql.Update;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Handles User data from database
 */
public class UserRepositoryImpl implements UserRepository {

    private final EntityManagerFactory emf;
    public UserRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        emf.runInTransaction(em -> {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
        });
        return true;
    }

    /**
     * Creates or updates a User entity inside database
     * @param user entity of User class
     * @return managed entity
     * @throws IllegalArgumentException if argument user is null
     */
    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return emf.callInTransaction(em -> em.merge(user));
    }

    /**
     *
     * @param id primary key of userId
     * @return Optional User entity
     */
    @Override
    public Optional<User> getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        return Optional.ofNullable(emf.callInTransaction(em -> em.find(User.class, id)));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                "SELECT a FROM User a WHERE a.username = :username", User.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
