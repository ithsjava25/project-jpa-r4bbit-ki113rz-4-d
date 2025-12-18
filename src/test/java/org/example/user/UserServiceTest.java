package org.example.user;

import jakarta.persistence.EntityManagerFactory;

import org.example.*;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
     *
     * Metoden:
     * - Konfigurerar Hibernate med MySQL
     * - Skapar EntityManagerFactory
     * - Initierar UserRepository och UserService
     *
     * Syftet är att varje test ska starta med en korrekt konfigurerad testmiljö.
     */
    @BeforeEach
    void setUp() {

        var cfg =
            new HibernatePersistenceConfiguration("emf")
                .jdbcUrl("jdbc:mysql://localhost:3306/bulletin")
                .jdbcUsername("root")
                .jdbcPassword("root")
                .property("hibernate.hbm2ddl.auto", "update")
                .managedClasses(User.class, Post.class);

        emf = cfg.createEntityManagerFactory();
        var userRepo = new UserRepositoryImpl(emf);
        userService = new UserServiceImpl(userRepo);

    }

    /**
     * Körs efter varje test.
     *
     * Stänger EntityManagerFactory och frigör databaskopplingar
     * för att undvika minnesläckor och låsta resurser.
     */
    @AfterEach
    void tearDown() {
        if (emf != null) {
            emf.close();
        }

    }

    /**
     * Verifies that
     *      - a user is created
     *      - id is generated
     *      - username is built in the right way
     *      - password is saved
     */
    @Test
    void shouldCreateUser() {
        //given
        String username = "Fiffen_Biffen";
        String password = "ImAlwaysOneWeekBehind";

        //when
        Optional<User> optionalUser =
            userService.createUser(username, password);

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
     *      - an already created user can login with correct password
     */
    @Test
    void validateUser() {
        //given
        String firstName = "Sandra";
        String lastName = "Nelj";
        String password = "ILoveHouseFlipper";

        Optional<User> create = userService.createUser(firstName, lastName, password);
        assertThat(create).isPresent();

        String username = create.get().getUsername();

        //when
        boolean validated = userService.validateUser(username, password);

        //then
        assertThat(validated).isTrue();
    }

    /**
     * Verifies that
     *      - if username already exists in database, make a unique one by adding numbers.
     */
    @Test
    void makeUniqueUsername() {
        //given
        Optional<User> create = userService.createUser
            ("Daniel", "Mart","ImNotGoingToBeShadyWithThisDROPCommandIJustLearned");
        assertThat(create).isPresent();

        String username = create.get().getUsername();

        //when
        String uniqueUsername = userService.makeUniqueUsername(username);

        //then
        assertThat(uniqueUsername).isEqualTo(username + "1");

    }

    /**
     * Verifies that
     *      - if name is shorter than 3 characters, method will add number to name
     *      to ensure the username meets the username lenght requirements.
     */
    @DisplayName("Adds a number to the username to meet the minimum length requirement")
    @Test
    void shouldExtendShortUsername() {
        String shortName = "Ed";

        String username = userService.formatStringForUsername(shortName);

        assertThat(username.length()).isGreaterThanOrEqualTo(3);
        assertThat(username).startsWith(shortName);
    }

    /**
     * Verifies that
     *      - {@link UserService#formatStringForUsername(String)} only formats names that are smaller than 3 characters.
     */
    @DisplayName("Should not modify names that meet the minimum length requirement")
    @Test
    void shouldNotExtendLongName() {
        String longName = "Rudolph";

        String username = userService.formatStringForUsername(longName);

        assertThat(username).isEqualTo(longName);

    }

    /**
     * Verifies that
     *      - a user is deleted
     *      - the deleted user is unable to login
     */
    @Test
    void shouldDeleteUser() {
        Optional<User> create = userService.createUser
            ("Edvin", "Karl", "HowDidIEndUpInAGroupProjectWithABunchOfMillenials");
        assertThat(create).isPresent();

        User user = create.get();
        Long id = user.getUserId();


        boolean deleted = userService.deleteUser(id);

        assertThat(deleted).isTrue();
        assertThat(userService.validateUser(user.getUsername(), user.getPassword())).isFalse();

    }
}

//package org.example.user;
//
//import jakarta.persistence.EntityManagerFactory;
//
//import org.example.Entities.Post;
//import org.example.Entities.User;
//import org.example.Repositories.UserRepositoryImpl;
//import org.example.Services.UserService;
//import org.example.Services.UserServiceImpl;
//import org.hibernate.jpa.HibernatePersistenceConfiguration;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.condition.DisabledOnOs;
//import org.junit.jupiter.api.condition.OS;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
///**
// * Integration tests require a local MySQL instance.
// * Tests are intended to be run locally and are skipped in CI.
// */
//
//@DisabledOnOs(OS.LINUX)
//public class UserServiceTest {
//
//    private EntityManagerFactory emf;
//    private UserService userService;
//
//
//    /**
//     * Körs före varje test.
//     *
//     * Metoden:
//     * - Konfigurerar Hibernate med MySQL
//     * - Skapar EntityManagerFactory
//     * - Initierar UserRepository och UserService
//     *
//     * Syftet är att varje test ska starta med en korrekt konfigurerad testmiljö.
//     */
//    @BeforeEach
//    void setUp() {
//
//        var cfg =
//            new HibernatePersistenceConfiguration("emf")
//                .jdbcUrl("jdbc:mysql://localhost:3306/bulletin")
//                .jdbcUsername("root")
//                .jdbcPassword("root")
//                .property("hibernate.hbm2ddl.auto", "update")
//                .managedClasses(User.class, Post.class);
//
//        emf = cfg.createEntityManagerFactory();
//        var userRepo = new UserRepositoryImpl(emf);
//        userService = new UserServiceImpl(userRepo);
//
//    }
//
//    /**
//     * Körs efter varje test.
//     *
//     * Stänger EntityManagerFactory och frigör databaskopplingar
//     * för att undvika minnesläckor och låsta resurser.
//     */
//    @AfterEach
//    void tearDown() {
//        if (emf != null) {
//            emf.close();
//        }
//
//    }
//
//    /**
//     * Verifies that
//     *      - a user is created
//     *      - id is generated
//     *      - username is built in the right way
//     *      - password is saved
//     */
//    @Test
//    void shouldCreateUser() {
//        //given
//        String firstName = "Fiffen";
//        String lastName = "Friberg";
//        String password = "ImAlwaysOneWeekBehind";
//
//        //when
//        Optional<User> optionalUser =
//            userService.createUser(firstName, lastName, password);
//
//        //then
//        assertThat(optionalUser).isPresent();
//
//        User user = optionalUser.get();
//
//        assertThat(user).isNotNull();
//        assertThat(user.getUserId()).isNotNull();
//        assertThat(user.getUsername()).isEqualTo("FifFri");
//        assertThat(user.getPassword()).isEqualTo("ImAlwaysOneWeekBehind");
//
//    }
//
//    /**
//     * Verifies that
//     *      - an already created user can login with correct password
//     */
//    @Test
//    void validateUser() {
//        //given
//        String firstName = "Sandra";
//        String lastName = "Nelj";
//        String password = "ILoveHouseFlipper";
//
//        Optional<User> create = userService.createUser(firstName, lastName, password);
//        assertThat(create).isPresent();
//
//        String username = create.get().getUsername();
//
//        //when
//        boolean validated = userService.validateUser(username, password);
//
//        //then
//        assertThat(validated).isTrue();
//    }
//
//    /**
//     * Verifies that
//     *      - if username already exists in database, make a unique one by adding numbers.
//     */
//    @Test
//    void makeUniqueUsername() {
//        //given
//        Optional<User> create = userService.createUser
//            ("Daniel", "Mart","ImNotGoingToBeShadyWithThisDROPCommandIJustLearned");
//        assertThat(create).isPresent();
//
//        String username = create.get().getUsername();
//
//        //when
//        String uniqueUsername = userService.makeUniqueUsername(username);
//
//        //then
//        assertThat(uniqueUsername).isEqualTo(username + "1");
//
//    }
//
//    /**
//     * Verifies that
//     *      - if name is shorter than 3 characters, method will add number to name
//     *      to ensure the username meets the username lenght requirements.
//     */
//    @DisplayName("Adds a number to the username to meet the minimum length requirement")
//    @Test
//    void shouldExtendShortUsername() {
//        String shortName = "Ed";
//
//        String username = userService.formatStringForUsername(shortName);
//
//        assertThat(username.length()).isGreaterThanOrEqualTo(3);
//        assertThat(username).startsWith(shortName);
//    }
//
//    /**
//     * Verifies that
//     *      - {@link UserService#formatStringForUsername(String)} only formats names that are smaller than 3 characters.
//     */
//    @DisplayName("Should not modify names that meet the minimum length requirement")
//    @Test
//    void shouldNotExtendLongName() {
//        String longName = "Rudolph";
//
//        String username = userService.formatStringForUsername(longName);
//
//        assertThat(username).isEqualTo(longName);
//
//    }
//
//    /**
//     * Verifies that
//     *      - a user is deleted
//     *      - the deleted user is unable to login
//     */
//    @Test
//    void shouldDeleteUser() {
//        Optional<User> create = userService.createUser
//            ("Edvin", "Karl", "HowDidIEndUpInAGroupProjectWithABunchOfMillenials");
//        assertThat(create).isPresent();
//
//        User user = create.get();
//        Long id = user.getUserId();
//
//
//        boolean deleted = userService.deleteUser(id);
//
//        assertThat(deleted).isTrue();
//        assertThat(userService.validateUser(user.getUsername(), user.getPassword())).isFalse();
//
//    }
//}
//
