package org.example;

/**
 * Handles anything related to users
 * Calls UserRepository if needed
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(String firstName, String lastName, String password) {
        String username = createUserName(firstName, lastName);
        User user = new User(firstName, lastName, username, password);
        return userRepo.save(user);
    }
    @Override
    public boolean validateUser(String username, String password) {
        return false;
    }

    public String createUserName(String firstName, String lastName) {
        return firstName.substring(0,3) + lastName.substring(0,3);
    }

    @Override
    public boolean updatePassword(String username, String password) {
        return false;
    }


}
