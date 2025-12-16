package org.example;

import jakarta.persistence.EntityManagerFactory;

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
     *
     * @param firstName Users first name
     * @param lastName Users last name
     * @param password User created password
     * @return managed User entity
     */
    public Optional<User> createUser(String firstName, String lastName, String password) {
        if (firstName == null || firstName.isBlank()
            || lastName == null || lastName.isBlank()
            || password == null || password.isBlank()) {
            return Optional.empty();
        }
        String formattedFirstName = formatStringForUsername(firstName);
        String formattedLastName = formatStringForUsername(lastName);
        String username = createUserName(formattedFirstName, formattedLastName);

        String uniqueUsername = makeUniqueUsername(username);

        User user = new User(firstName, lastName, uniqueUsername, password);
        return Optional.ofNullable(userRepo.save(user));
    }


    /**
     *
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
     *
     * @param firstName users first name
     * @param lastName users last name
     * @return String with unique username
     */
    @Override
    public String createUserName(String firstName, String lastName) {
        return firstName.substring(0,3) + lastName.substring(0,3);
    }

    /**
     * Produce a username that does not collide with existing accounts.
     *
     * @param userName the preferred username
     * @return the original username if available; otherwise the username with a numeric suffix that is not already used
     */
    @Override
    public String makeUniqueUsername(String userName){
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
     */
    @Override
    public String formatStringForUsername(String name) {
        StringBuilder tempName = new StringBuilder(name);
        int counter = 1;
        //Adds numbers at the end while name length is less than 3
        while (tempName.length() < 3) {
            tempName.append(counter);
            counter++;
        }
        return tempName.toString();
    }

    @Override
    public boolean updatePassword(String username, String newPassword) {

//        try (EntityManagerFactory emf = emf.createEntityManagerFactory()){
//            emf.runInTransaction(em -> {
//
//                var users = em.createQuery(
//                        "select u from User u where u.username = :username", User.class)
//                    .setParameter("username", username)
//                    .getResultList();
//
//                if (users.isEmpty()) {
//                    throw new IllegalArgumentException("User not found");
//                }
//                User user = users.get(0);
//                user.setPassword(newPassword);
//            });
//            return true;
//
//        } catch (Exception e) {
            return false;
//        }
    }

    @Override
    public boolean deleteUser(Long id) {
        userRepo.deleteUser(id);
        return true;
    }


}
