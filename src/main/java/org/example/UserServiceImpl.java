package org.example;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     *
     * @param firstName Users first name
     * @param lastName Users last name
     * @param password User created password
     * @return managed User entity
     */
    public Optional<User> createUser(String firstName, String lastName, String password) {
        if (firstName == null || firstName.isBlank()
            || lastName == null || lastName.isBlank()
            || password == null || password.isBlank()) {
            throw new IllegalArgumentException("First name, last name, and password are required");
        }

        String username = createUserName(firstName, lastName);

        if(userRepo.getUserByUsername(username).isPresent()) {
            return Optional.empty();
        }
        User user = new User(firstName, lastName, username, password);
        return Optional.ofNullable(userRepo.save(user));
    }


    /**
     *
     * @param username App users username
     * @param password App users password
     * @return true if username and password match is found. Return false if blank or null or no match found
     */
    @Override
    public boolean validateUser(String username, String password) {
        if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {
            return false;
        }

        return userRepo.getUserByUsername(username)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);
    }

    public String createUserName(String firstName, String lastName) {
        return firstName.substring(0,3) + lastName.substring(0,3);
    }

    @Override
    public boolean updatePassword(String username, String newPassword) {

//        try (EntityManagerFactory emf = emf.createEntityManagerFactory()){
//            emf.runInTransaction(em -> {
//
//                var users = em.createQuery(
//                        "select u from User u where u.username = :username", User.class)
//                    .setParameter("username", username)
//                    .getResultList();
//
//                if (users.isEmpty()) {
//                    throw new IllegalArgumentException("User not found");
//                }
//                User user = users.get(0);
//                user.setPassword(newPassword);
//            });
//            return true;
//
//        } catch (Exception e) {
            return false;
//        }
    }

    @Override
    public boolean deleteUser(Long id) {
        userRepo.deleteUser(id);
        return true;
    }


}
