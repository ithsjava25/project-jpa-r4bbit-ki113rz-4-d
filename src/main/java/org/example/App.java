package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

import java.util.Objects;

/**
 * Use launcher to launch the App class
 * This is the "main"
 *
 * Creates a PersistenceConfiguration
 * With the PersistenceConfiguration create a EntityManagerFactory(emf)
 * Alternative is using JPAUtil with persistence.xml
 *
 * Inject the Repos and Services with the EntityManagerFactory
 * Creates a fxml loader (fxmlLoader)
 * Uses the loader to load the UI inside the fxml
 * Finally sets a scene with the fxml UI
 */

public class App extends Application {
    private EntityManagerFactory emf;

    @Override
    public void start(Stage stage) throws Exception {

        //Creates a connection config with database
        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/bulletin")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(User.class, Post.class);

        //Creates an EntityManager with the config
        emf = cfg.createEntityManagerFactory();

        //Initialize Repositories
        UserRepositoryImpl userRepo = new UserRepositoryImpl(emf);
        PostRepository postRepo = new PostRepositoryImpl(emf);

        //Initialize Services
        PostService postService = new PostServiceImpl(postRepo, userRepo);
        UserService userService = new UserServiceImpl(userRepo);

        //Load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/BulletinView.fxml"));
        Parent root = fxmlLoader.load();

        //Initialize controller
        Controller controller = fxmlLoader.getController(); //Creates a controller instance
        controller.setUserService(userService, postService); //That is injected with userService

        //Show stage
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
            Objects.requireNonNull(App.class.getResource("/css/board.css")
            ).toExternalForm());
        stage.setTitle("Bulletin Board");
        stage.setScene(scene);
        stage.show();


        boolean updated = userService.updatePassword("admadm", "newPassword");
        System.out.println("Password updated: " + updated);
        Long userId = 3L;
        boolean deleteUser = userService.deleteUser(userId);
        if (deleteUser) {
            System.out.println("Deleted: User " + userId);
        } else {
            System.out.println("User not found: " + userId);
        }

        // Validates user credentials by verifying the username and password against the database
        boolean isValid = userService.validateUser("admadm", "admin");

        if (isValid) {
            System.out.println("Login OK");
        } else {
            System.out.println("Invalid username or password");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        if (emf != null) {
            emf.close();
        }
    }
}
