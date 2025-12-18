package org.example.Repositories;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.User;

import java.util.Optional;

/**
 * Handles User data from database
 */
public class UserRepositoryImpl implements UserRepository {

    private final EntityManagerFactory emf;
    public UserRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }


    /**
     * Deletes a user from database based on userID
     * @param id users userId
     * @return true if user was deleted, returns false if fail
     * @throws IllegalArgumentException if argument id is null
     */
    @Override
    public boolean deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return emf.callInTransaction(em -> {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
                return true;
            } else {
                return false;
            }
        });
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
     *  Finds and returns user based on userID
     * @param id primary key of userId
     * @return Optional User entity or Optional.empty() if no match
     * @throws IllegalArgumentException if argument id is null
     */
    @Override
    public Optional<User> getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable(emf.callInTransaction(em -> em.find(User.class, id)));
    }

    /**
     *  Find and returns user based on username
     * @param username users username
     * @return Optional<User> entity or Optional.empty() if no match
     * @throws IllegalArgumentException if argument username is null
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        return (emf.callInTransaction(em -> em.createQuery(
            "SELECT u FROM User u WHERE u.username = :username", User.class)
        .setParameter("username", username)
        .getResultStream()
        .findFirst())); //Returns Optional.empty() if not found
    }
}
