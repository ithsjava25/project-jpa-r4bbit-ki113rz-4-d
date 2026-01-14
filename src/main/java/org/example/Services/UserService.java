package org.example.Services;

import org.example.Entities.User;
import java.util.Optional;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public interface UserService {

    void updateBio(User user, String newBio);
    boolean validateUser(String username, String password);
    Optional<User> createUser(String firstName, String lastName, String password, String username, String confirmPassword);
    boolean updatePassword(String username, String oldPassword, String newPassword, String confirmNewPassword);

    Optional<User> login(String username, String password);
    Optional<User> getUserById(Long id);

    void updateName(User user, String newFirstName, String newLastName);

    boolean deleteUser(Long id);

    void updateUsername(User user, String newUsername);

    Optional<User> getUserByUsername(String username);
}

