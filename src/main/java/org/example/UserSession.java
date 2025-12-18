package org.example;

import org.example.Entities.User;

import java.util.Optional;

public final class UserSession {

    private static User currentUser;

    private UserSession() {}

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
