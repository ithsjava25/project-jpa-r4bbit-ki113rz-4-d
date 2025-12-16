package org.example;

public interface UserService {
    boolean validateUser(String username, String password);
    String createUserName(String firstName, String lastName);
    User createUser(String firstName, String lastName, String password);
    boolean updatePassword(String username, String password);
}

