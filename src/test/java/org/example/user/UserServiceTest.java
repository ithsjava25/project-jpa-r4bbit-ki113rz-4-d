package org.example.user;

import jakarta.persistence.EntityManagerFactory;

import org.example.*;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
                .managedClasses(User.class);

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
        String firstName = "Fiffen";
        String lastName = "Friberg";
        String password = "ImAlwaysOneWeekBehind";

        //when
        User user = userService.createUser(firstName, lastName, password);

        //then
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo("FifFri");
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
        String lastName = "Neljestam";
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
     *      - if username already exists in database, make a unique one
     */
    @Test
    void makeUniqueUsername() {
        //given
        Optional<User> create = userService.createUser("Ash", "Ketchum","GottaCatchEmAll");
        assertThat(create).isPresent();

        String username = create.get().getUsername();

        //when
        String uniqueUsername = userService.makeUniqueUsername(username);

        //then
        assertThat(uniqueUsername).isEqualTo(username + "1");

    }
}
