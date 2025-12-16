package org.example;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public interface UserService {
    boolean validateUser(String username, String password);
    String createUserName(String firstName, String lastName);
    User createUser(String firstName, String lastName, String password);
    boolean updatePassword(String username, String password);

    boolean deleteUser(Long id);
}

