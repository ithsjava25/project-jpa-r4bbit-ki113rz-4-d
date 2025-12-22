package org.example.user;

import org.example.Entities.User;
import org.example.UserSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


//todo: add userMustLoginAgainAfterLogout

class UserSessionTest {

    @AfterEach
    void clearSession() {
        UserSession.logout();
    }



    /**
     * Verifies that
     *      - session is empty at start
     */
    @Test
    void shouldNotHaveUserLoggedInInitially() {
        assertThat(UserSession.isLoggedIn()).isFalse();
        assertThat(UserSession.getCurrentUser()).isEmpty();
    }

    /**
     * Verifies that
     *      - session gets current user when logged in
     */
    @Test
    void shouldLoginUser() {
        User user = new User("Scooby", "Doo", "ScoobySnaxxx", "scoobydoobydoo");

        UserSession.login(user);

        assertThat(UserSession.isLoggedIn()).isTrue();
        assertThat(UserSession.getCurrentUser()).contains(user);
    }

    /**
     * Verifies that
     *      - currentUser is set to null when logged out
     */
        @Test
        void shouldLogoutUserFromSession() {
            User user = new User("Norville", "Rogers", "Shaggy", "password123");

            UserSession.login(user);
            assertThat(UserSession.isLoggedIn()).isTrue();

            UserSession.logout();

            assertThat(UserSession.isLoggedIn()).isFalse();
            assertThat(UserSession.getCurrentUser()).isEmpty();
        }

    }

