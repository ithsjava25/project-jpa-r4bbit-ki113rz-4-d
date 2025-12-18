package org.example;

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
     * then returns it
     * @param firstName Users first name
     * @param lastName Users last name
     * @param password User created password
     * @return managed User entity
     */
    @Override
    public Optional<User> createUser(String firstName, String lastName, String password) {
        if (firstName == null || firstName.isBlank()
            || lastName == null || lastName.isBlank()
            || password == null || password.isBlank()) {
            return Optional.empty();
        }

        String username = createUserName(firstName, lastName);

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
        if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {
            return false;
        }

        return userRepo.getUserByUsername(username)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);
    }

    /**
     * Creates a username with the first 3 characters from firstName
     * and first 3 characters from lastName
     * Makes sure username is unique by returning result of makeUniqueUsername()(adds numbers at end);
     * Makes sure firstname and lastname is correct format 3 letters or bigger (adds numbers at end)
     * @param firstName users first name
     * @param lastName users last name
     * @return String with username
     * @throws IllegalArgumentException if any of the arguments are null or blank
     */
    @Override
    public String createUserName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("firstName and lastName must not be null or blank");
        }

        String username = formatStringForUsername(firstName).substring(0,3)
            + formatStringForUsername(lastName).substring(0,3);
        return makeUniqueUsername(username);
    }

    /**
     * Produce a username that does not collide with existing accounts.
     *
     * @param userName the preferred username
     * @return the original username if available; otherwise the username with a numeric suffix that is not already used
     * @throws IllegalArgumentException if argument userName is null or blank
     */
    @Override
    public String makeUniqueUsername(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("userName is null or blank");
        }
        int counter = 1;
        String newUserName = userName;

        //Adds numbers at the end if username exists
        //Try adding 1, then 2, then 3 etc...
        while (userRepo.getUserByUsername(newUserName).isPresent()) {
            newUserName = userName + counter;
            counter++;
        }
        return newUserName;
    }

    /**
     * Ensure a name is at least three characters by appending incremental digits.
     *
     * @param name the original name to format; may be shorter than three characters
     * @return the name padded with incremental digits starting at 1 until its length is at least three
     * @throws IllegalArgumentException if argument name is null or blank
     */
    @Override
    public String formatStringForUsername(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is null or blank");
        }
        StringBuilder tempName = new StringBuilder(name);
        int counter = 1;
        //Adds numbers at the end while name length is less than 3
        while (tempName.length() < 3) {
            tempName.append(counter);
            counter++;
        }
        return tempName.toString();
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
        if (username == null || username.isBlank()
            || password == null || password.isBlank()) {
            return Optional.empty();
        }

        if (validateUser(username, password)) {
            return userRepo.getUserByUsername(username);
        } else {
            return Optional.empty();
        }
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
