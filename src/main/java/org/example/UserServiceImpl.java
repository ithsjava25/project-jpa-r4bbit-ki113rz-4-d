package org.example;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

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
        return userRepo.getUserByUsername(username)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);
    }

    public String createUserName(String firstName, String lastName) {
        return firstName.substring(0,3) + lastName.substring(0,3);
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
