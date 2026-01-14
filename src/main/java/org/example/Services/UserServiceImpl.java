package org.example.Services;

import org.example.Entities.Profile;
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
    public Optional<User> createUser(String firstName, String lastName, String password, String username, String confirmPassword) {
        if (firstName == null || firstName.isBlank()
            || lastName == null || lastName.isBlank()
            || password == null || password.isBlank()
            || username == null || username.isBlank()
            || confirmPassword == null || confirmPassword.isBlank()) {
            return Optional.empty();
        }

        if (username.contains(" ")) {
            return Optional.empty();
        }

        if (password.length() <= 3 || password.length() >= 64){
            return Optional.empty();
        }

        if (!password.equals(confirmPassword)) {
            return Optional.empty();
        }

        if (username.length() <= 2 || username.length() >= 30) {
            return Optional.empty();
        }
        if (userRepo.getUserByUsername(username).isPresent()) {
            return Optional.empty();
        }

        User user = new User(firstName, lastName, username, password);

        Profile profile = new Profile();
        profile.setBio("Hello " + user.getFirst_name() + "! Welcome to your bio, write something about yourself: ");

        profile.setUser(user);
        user.setProfile(profile);

        return Optional.ofNullable(userRepo.save(user));
    }


    @Override
    public void updateBio(User user, String newBio) {

        //This is here mostly because if the user was created before the profile was implemented
        if (user.getProfile() == null) {
            Profile profile = new Profile();
            profile.setUser(user);
            profile.setBio("");
            user.setProfile(profile);
        }

        user.getProfile().setBio(newBio);
        userRepo.save(user);
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
    public boolean updatePassword(String username, String oldPassword, String newPassword, String confirmNewPassword) {
        if (username == null || username.isBlank()
            || oldPassword == null || oldPassword.isBlank()
            || newPassword == null || newPassword.isBlank()
            || confirmNewPassword == null || confirmNewPassword.isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }
        return userRepo.getUserByUsername(username)
            .map(user -> {
                if (!user.getPassword().equals(oldPassword)) {
                    return false;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    return false;
                }

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

    @Override
    public void updateName(User user, String newFirstName, String newLastName) {
        if(user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if(newFirstName == null || newFirstName.isBlank()) {
            throw new IllegalArgumentException("First name is null or blank");
        }
        if(newLastName == null || newLastName.isBlank()) {
            throw new IllegalArgumentException("Last name is null or blank");
        }
        userRepo.updateName(user, newFirstName, newLastName);
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

    @Override
    public void updateUsername(User user, String newUsername) {
        if(user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if(newUsername == null || newUsername.isBlank()) {
            throw new IllegalArgumentException("Username is null or blank");
        }
        userRepo.updateUsername(user, newUsername);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }
}
