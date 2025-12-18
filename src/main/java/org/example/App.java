package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Controllers.Controller;
import org.example.Controllers.LoginController;
import org.example.Controllers.RegisterController;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repositories.PostRepository;
import org.example.Repositories.PostRepositoryImpl;
import org.example.Repositories.UserRepositoryImpl;
import org.example.Services.PostService;
import org.example.Services.PostServiceImpl;
import org.example.Services.UserService;
import org.example.Services.UserServiceImpl;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

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
    private static App app;
    private EntityManagerFactory emf;
    private UserService userService;
    private PostService postService;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        app = this;

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
        postService = new PostServiceImpl(postRepo, userRepo);
        userService = new UserServiceImpl(userRepo);

        showLogin();
        stage.show();
    }


    /**
     * @return an instance of the current App
     */
    public static App getAppInstance() {
        return app;
    }

    /**
     * Loads LoginView fxml file
     * Initializes LoginController
     * Gets stylesheet login.css
     * Sets the stage as login scene
     */
    public void showLogin() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setUserService(userService);

            Scene scene = new Scene(root, 400, 300);
            scene.getStylesheets()
                .add(getClass().getResource("/css/login.css").toExternalForm());

            stage.setTitle("Login");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads RegisterView fxml file
     * Initializes RegisterController
     * Gets stylesheet register.css
     * Sets the stage as register scene
     */
    public void showRegister() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/RegisterView.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setUserService(userService);

            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets()
                .add(getClass().getResource("/css/register.css").toExternalForm());
            stage.setTitle("Register");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads BulletinView fxml file
     * Initializes Controller
     * Gets stylesheet board.css
     * Sets the stage as bulletin board scene
     */
    public void showBoard() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/BulletinView.fxml"));
            Parent root = loader.load();

            Controller controller = loader.getController();
            controller.setUserService(userService, postService);

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets()
                .add(getClass().getResource("/css/board.css").toExternalForm());

            stage.setTitle("Bulletin Board");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        if (emf != null) {
            emf.close();
        }
        UserSession.logout();
    }
}
