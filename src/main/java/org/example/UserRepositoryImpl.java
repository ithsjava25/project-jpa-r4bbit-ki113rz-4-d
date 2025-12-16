package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.*;
import org.hibernate.sql.Update;

import java.math.BigDecimal;

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
     */
    @Override
    public User save(User user) {
        return emf.callInTransaction(em -> em.merge(user));
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }
    // Validates user credentials by verifying the username and password against the database




}
