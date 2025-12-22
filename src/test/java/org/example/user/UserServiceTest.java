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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

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
 *     Password lenght must be between 8 and 64 characters
 *     A user can authenticate using a valid username and password
 *     Password must be case-sensitive
 *     Username must be case-insensitive
 *     A user can be deleted from the system
 *     A user can change their password
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

    //==========//Create user//==========//

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
    /** Input validation tests
     * These tests verify that
     *              - firstname input cannot be empty
     *              - lastname input cannot be empty
     *              - password input cannot be empty
     *              - username input cannot be empty
     *
     *              - password cannot be under 8 characters
     *              - password cannot be over 64 characters
     *              - username cannot be under 3 characters
     *              - username cannot be over 30 characters
     *
     *              - password cannot contain blank spaces
     *              - username cannot contain blank spaces
     *
     */
    @DisplayName("Must force user to fill in all necessary fields")
    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("invalidUserInputs")
    void shouldNotCreateuserWheninvalidInput(
        String reason,
        String firstName,
        String lastName,
        String password,
        String username
    ) {
        Optional<User> result =
            userService.createUser(firstName, lastName, password, username);

        assertThat(result).isEmpty();
    }
    static Stream<Arguments> invalidUserInputs() {
        return Stream.of(
            Arguments.of("firstName input cannot be blank", "", "Friberg", "secret123", "fiffen"),
            Arguments.of("lastName input cannot be blank", "Fiffen", "", "secret123", "fiffen"),
            Arguments.of("password input cannot be blank", "Fiffen", "Friberg", "", "fiffen"),
            Arguments.of("username input cannot blank", "Fiffen", "Friberg", "secret123", ""),
            Arguments.of("password must be 8 or more characters", "Fiffen", "Friberg", "secret", "fiffen"),
            Arguments.of("password must be a maximum of 64 characters", "Fiffen", "Friberg", "s".repeat(65), "fiffen"),
            Arguments.of("username must be 3 or more characters", "Fiffen", "Friberg", "secret123", "Fi"),
            Arguments.of("username must be a maximum of 30 characters", "Fiffen", "Friberg", "secret123", "f".repeat(31)),
            Arguments.of("password input cannot contain blank spaces", "Fiffen", "Friberg", "secret 123", "fiffen"),
            Arguments.of("username input cannot contain blank spaces", "Fiffen", "Friberg", "secret123", "f ifen")
        );
    }







//==========// login verification //=========//
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
     *      - password must be typed exactly as set
     */
    @Test
    void passwordIsCaseSensitive() {
        Optional<User> create = userService.createUser(
            "Daniel", "Mart", "ImNotDoingAnythingShadyWithThisDROPCommandIJustLearned", "Maxxer"
        );
        assertThat(create).isPresent();

        boolean authorize = userService.validateUser("maxxer","imnotdoinganythingshadywiththisdropcommandijustlearned");

        assertThat(authorize).isFalse();
    }

    /**
     * Verifies that
     *      - username is valid even if typed in different casing than set username
     */
    @Test
    void usernameIsNotCaseSensitive() {
        Optional<User> create = userService.createUser(
            "Alban", "Nwapa", "SingHalleluja", "Dr.Alban"
        );
        assertThat(create).isPresent();

        boolean authorize = userService.validateUser("dr.alBAN","SingHalleluja");

        assertThat(authorize).isTrue();
    }




    //==========//Update Current User//==========//

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

    /**
     * Verifies that
     *      - the user is able to change password
     */
    @Test
    void shouldChangePassword() {
        Optional<User> create = userService.createUser(
            "Linus","Torva", "talkIsCheapShowMeTheCode", "LinuxxUserNo1"
        );
        assertThat(create).isPresent();

        User user = create.get();
        String oldPassword = user.getPassword();
        String newPassword = "givenEnoughEyeballsAllBugsAreShallow";

        boolean updated = userService.updatePassword(user.getUsername(), newPassword);

        assertThat(updated).isTrue();
        assertThat(userService.validateUser(user.getUsername(), oldPassword)).isFalse();
        assertThat(userService.validateUser(user.getUsername(), newPassword)).isTrue();


    }
}


