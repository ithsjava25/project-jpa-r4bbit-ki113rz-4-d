package org.example.user;

import jakarta.persistence.EntityManagerFactory;

import org.example.*;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void validateUser() {
        //given
        String firstName = "Sandra";
        String lastName = "Neljestam";
        String password = "ILoveHouseFlipper";

        User created = userService.createUser(firstName, lastName, password);
        String username = created.getUsername();

        //when
        User validated = userService.validateUser(username, password);

        //then
        assertThat(validated).isNotNull();
        assertThat(validated.getUsername()).isEqualTo(username);
    }
}
