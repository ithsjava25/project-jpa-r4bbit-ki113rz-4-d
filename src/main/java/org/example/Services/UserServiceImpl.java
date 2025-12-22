package org.example.Services;

import org.example.Entities.User;
import org.example.Repositories.UserRepository;

import java.util.Optional;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Takes parameters and creates a user and saves it inside the database
     * checks if username is unique
     * then returns it
     * @param firstName Users first name
     * @param lastName Users last name
     * @param password User created password
     * @param username Users preferred username
     * @return managed User entity
     */
    @Override
    public Optional<User> createUser(String firstName, String lastName, String username, String password) {

        if (firstName == null || firstName.isBlank()
            || lastName == null || lastName.isBlank()
            || password == null || password.isBlank()
            || username == null || username.isBlank()) {
            return Optional.empty();
        }

        if (username.length() <= 2 || username.length() >= 30) {
            return Optional.empty();
        }
        if (userRepo.getUserByUsername(username).isPresent()) {
            return Optional.empty();
        }

        User user = new User(firstName, lastName, username, password);
        return Optional.ofNullable(userRepo.save(user));
    }


    /**
     * Checks if user exists in database,
     * then tests if the password is a match
     * @param username App users username
     * @param password App users password
     * @return true if username and password match is found. Return false if blank or null or no match found
     */
    @Override
    public boolean validateUser(String username, String password) {
        return userRepo.getUserByUsername(username)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);
    }


    /**
     * Sets new password for user with username
     * @param username users username
     * @param newPassword the new password
     * @return true if successful, returns false if no user is found
     */
    @Override
    public boolean updatePassword(String username, String newPassword) {
        if (username == null || username.isBlank() ||
            newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }
        return userRepo.getUserByUsername(username)
            .map(user -> {
                user.setPassword(newPassword);
                userRepo.save(user);
                return true;
            })
            .orElse(false);
    }

    @Override
    public Optional<User> login(String username, String password) {
        if (isInvalid(username) || isInvalid(password)) {
            return Optional.empty();
        }
        if (validateUser(username, password)) {
            return userRepo.getUserByUsername(username);
        }
        return Optional.empty();
    }

    private boolean isInvalid(String str) {
        return str == null || str.isBlank();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id is null");
        }
        return userRepo.getUserById(id);
    }

    /**
     * Deletes user with userId id
     * @param id users userID
     * @return true if successful, returns false if no user is found
     * @throws IllegalArgumentException if argument id is null
     */
    @Override
    public boolean deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (userRepo.getUserById(id).isPresent()) {
            userRepo.deleteUser(id);
            return true;
        } else {
            return false;
        }
    }
}
