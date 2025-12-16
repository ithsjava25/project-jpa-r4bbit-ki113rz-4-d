package org.example;

public interface UserRepository {


    boolean deleteUser(Long id);

    User save(User user);

    User getUserById(Long id);



}


