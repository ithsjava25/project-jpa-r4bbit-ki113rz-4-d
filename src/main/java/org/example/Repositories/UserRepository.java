package org.example.Repositories;

import org.example.Entities.User;

import java.util.Optional;

/**
 * Handles User data from database
 */
public interface UserRepository {

    boolean deleteUser(Long id);

    User save(User user);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);


}


