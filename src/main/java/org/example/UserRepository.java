package org.example;

/**
 * Handles User data from database
 */
public interface UserRepository {

    boolean deleteUser(Long id);

    User save(User user);

    User getUserById(Long id);



}


