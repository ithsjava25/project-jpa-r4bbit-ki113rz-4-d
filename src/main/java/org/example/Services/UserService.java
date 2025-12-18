package org.example;

import java.util.Optional;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public interface UserService {
    boolean validateUser(String username, String password);
    String createUserName(String firstName, String lastName);
    Optional<User> createUser(String firstName, String lastName, String password);
    boolean updatePassword(String username, String password);

    Optional<User> login(String username, String password);
    Optional<User> getUserById(Long id);
    String makeUniqueUsername(String userName);
    String formatStringForUsername(String name);

    boolean deleteUser(Long id);

}

