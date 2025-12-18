package org.example.Services;

import org.example.Entities.User;

import java.util.Optional;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public interface UserService {
    boolean validateUser(String username, String password);
    Optional<User> createUser(String firstName, String lastName, String password, String username);
    boolean updatePassword(String username, String password);

    Optional<User> login(String username, String password);
    Optional<User> getUserById(Long id);

    boolean deleteUser(Long id);

}

