package org.example;

import java.util.Optional;

/**
 * Handles User data from database
 */
public interface UserRepository {

    boolean deleteUser(Long id);

    User save(User user);

    User getUserById(Long id);

    Optional<User> getUserByUsername(String username);


}


