package org.example;

public interface UserRepository {
boolean createUser(String firstName, String lastName, String password);
boolean deleteUser(Long id);
boolean validateUser(String username, String password);
boolean updatePassword(String username, String password);

String createUserName(String firstName, String lastName);
}
