package org.example.user;

import jakarta.persistence.EntityManagerFactory;

import org.example.*;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repositories.UserRepositoryImpl;
import org.example.Services.UserService;
import org.example.Services.UserServiceImpl;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_ARRAY;

//==========//==========//
/*
 * Integration tests for UserService.
 *
 *
 * These tests verify the business rules and expected behavior
 * for users in the system.
 *
 *
 *                      User rules
 *
 *     Username is provided by the user and must be unique
 *     Username length must be between 3 and 30 characters
 *     Password must not be null or blank
 *     A user can authenticate using a valid username and password
 *     A user can be deleted from the system
 *
 */
//==========//==========//

/**
 * Integration tests require a local MySQL instance.
 * Tests are intended to be run locally and are skipped in CI.
 */

@DisabledOnOs(OS.LINUX)
public class UserServiceTest {

    private EntityManagerFactory emf;
    private UserService userService;


    /**
     * Körs före varje test.
     * <p>
     * Metoden:
     * - Konfigurerar Hibernate med MySQL
     * - Skapar EntityManagerFactory
     * - Initierar UserRepository och UserService
     * <p>
     * Syftet är att varje test ska starta med en korrekt konfigurerad testmiljö.
     */
    @BeforeEach
    void setUp() {

        var cfg =
            new HibernatePersistenceConfiguration("emf")
                .jdbcUrl("jdbc:mysql://localhost:3306/bulletin_test")
                .jdbcUsername("root")
                .jdbcPassword("root")
                .property("hibernate.hbm2ddl.auto", "update")
                .managedClasses(User.class, Post.class, Category.class);

        emf = cfg.createEntityManagerFactory();
        var userRepo = new UserRepositoryImpl(emf);
        userService = new UserServiceImpl(userRepo);

    }

    /**
     * Körs efter varje test.
     * <p>
     * Stänger EntityManagerFactory och frigör databaskopplingar
     * för att undvika minnesläckor och låsta resurser.
     */
    @AfterEach
    void tearDown() {
        if (emf != null) {
            emf.runInTransaction(em -> {
                em.createQuery("DELETE FROM User").executeUpdate();
            });
            emf.close();
        }
    }

    /**
     * Verifies that
     * - a user is created
     * - id is generated
     * - password is saved
     */
    @Test
    void shouldCreateUser() {
        //given
        String firstName = "Fiffen";
        String lastName = "Friberg";
        String username = "Fiffen_Biffen";
        String password = "ImAlwaysOneWeekBehind";

        //when
        Optional<User> optionalUser =
            userService.createUser(firstName, lastName, password, username);

        //then
        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo("ImAlwaysOneWeekBehind");

    }

    /**
     * Verifies that
     * - an already created user can login with correct password
     */
    @Test
    void validateUser() {
        //given
        String firstName = "Sandra";
        String lastName = "Nelj";
        String username = "Sandra";
        String password = "ILoveHouseFlipper";

        Optional<User> create = userService.createUser(firstName, lastName, password, username);
        assertThat(create).isPresent();

        String getUsername = create.get().getUsername();

        //when
        boolean validated = userService.validateUser(getUsername, password);

        //then
        assertThat(validated).isTrue();
    }

    /**
     * Verifies that
     * - a user is deleted
     * - the deleted user is unable to login
     */
    @Test
    void shouldDeleteUser() {
        Optional<User> create = userService.createUser
            ("Edvin", "Karl", "HowDidIEndUpInAGroupProjectWithABunchOfMillenials", "RabbitDude");
        assertThat(create).isPresent();

        User user = create.get();
        Long id = user.getUserId();


        boolean deleted = userService.deleteUser(id);

        assertThat(deleted).isTrue();
        assertThat(userService.validateUser(user.getUsername(), user.getPassword())).isFalse();
    }
}


