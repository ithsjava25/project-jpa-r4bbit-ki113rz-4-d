package org.example;

import jakarta.persistence.*;
import org.hibernate.sql.Update;

import java.math.BigDecimal;

public class UserRepositoryImpl implements UserRepository {

    private final PersistenceConfiguration cfg;
    public UserRepositoryImpl(PersistenceConfiguration cfg) {
        this.cfg = cfg;
    }
    @Override
    public boolean createUser(String firstName, String lastName, String password) {

        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {

//            Insert/Persist of new entity
//            Update of managed entity
            emf.runInTransaction(em -> {
                User user = new User();
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setPassword(password);
                user.setUsername(createUserName(firstName, lastName));
                em.persist(user);
            });
        }
        return false;
    }

    public String createUserName(String firstName, String lastName) {
        return firstName.substring(0,3) + lastName.substring(0,3);
    }

    @Override
    public boolean deleteUser(Long id) {
        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {

            emf.runInTransaction(em -> {
                User user = em.find(User.class, id);
                if (user != null) {
                    em.remove(user);
                }
            });
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
        }
    }

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
