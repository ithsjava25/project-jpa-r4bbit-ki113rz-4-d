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
    // Validates user credentials by verifying the username and password against the database
    @Override
    public boolean validateUser(String username, String password) {
        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {
            EntityManager em = emf.createEntityManager();

            try {
                TypedQuery<User> query = em.createQuery("SELECT a FROM User a WHERE a.username = :username",
                    User.class);
                query.setParameter("username", username);
                User user = query.getSingleResult();
                return  (user.getPassword().equals(password));


            }catch (NoResultException e) {
                return false;

        } finally {
            em.close();
        }
        }

    }

    @Override
    public boolean updatePassword(String username, String newPassword) {

        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()){
            emf.runInTransaction(em -> {

                var users = em.createQuery(
                    "select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultList();

                if (users.isEmpty()) {
                    throw new IllegalArgumentException("User not found");
                }
                User user = users.get(0);
                user.setPassword(newPassword);
            });
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
