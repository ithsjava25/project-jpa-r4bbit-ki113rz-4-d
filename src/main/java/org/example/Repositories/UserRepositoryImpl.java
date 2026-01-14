package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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
            throw new IllegalArgumentException("Id cannot be null");
        }
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, id);
        if (user!=null) {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return true;
        }
            return false;
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
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (user.getUserId() == null) {
                em.persist(user);
            } else {
                user = em.merge(user);
            }
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
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
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst(); //Returns Optional.empty() if not found
        } finally {
            em.close();

        }
    }

    @Override
    public void updateName(User user, String newFirstName, String newLastName) {
        if(user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if(newFirstName == null || newFirstName.isBlank()) {
            throw new IllegalArgumentException("First name is null or blank");
        }
        if(newLastName == null || newLastName.isBlank()) {
            throw new IllegalArgumentException("Last name is null or blank");
        }
        user.setFirst_name(newFirstName);
        user.setLast_name(newLastName);
        save(user);
    }

    @Override
    public void updateUsername(User user, String newUsername) {
        if(user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if(newUsername == null || newUsername.isBlank()) {
            throw new IllegalArgumentException("Username is null or blank");
        }
        user.setUsername(newUsername);
        save(user);
    }
}
