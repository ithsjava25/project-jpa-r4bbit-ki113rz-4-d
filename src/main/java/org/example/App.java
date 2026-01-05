package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.Controllers.Controller;
import org.example.Controllers.LoginController;
import org.example.Controllers.ProfileController;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.Repositories.*;
import org.example.Services.*;
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
    private CategoryService categoryService;
    private ProfileService profileService;
    private Stage stage;

    private Image iconTopLeft;

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
            .managedClasses(User.class, Post.class, Category.class, Profile.class);

        //Creates an EntityManager with the config
        emf = cfg.createEntityManagerFactory();

        //Initialize Repositories
        UserRepositoryImpl userRepo = new UserRepositoryImpl(emf);
        CategoryRepository categoryRepo = new CategoryRepositoryImpl(emf);
        PostRepository postRepo = new PostRepositoryImpl(emf);
        ProfileRepository profileRepo = new ProfileRepositoryImpl(emf);

        //Initialize Services
        this.userService = new UserServiceImpl(userRepo);
        this.categoryService = new CategoryServiceImpl(categoryRepo);
        this.postService = new PostServiceImpl(postRepo, categoryRepo);
        this.profileService = new ProfileServiceImpl(profileRepo);

        iconTopLeft = new Image(getClass().getResourceAsStream("/iths.png"));

        categoryService.seedDefaultCategories();

        System.out.println("Categories in Db");
        categoryService.getAllCategories().forEach(System.out::println);

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
            loginController.setUserService(userService, app);

            Scene scene = new Scene(root, 400, 300);
            scene.getStylesheets()
                .add(getClass().getResource("/css/login.css").toExternalForm());

            stage.setTitle("Login");
            stage.setScene(scene);
            stage.getIcons().add(iconTopLeft);

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

            org.example.Controllers.RegisterController controller = loader.getController();
            controller.setUserService(userService, profileService, app);

            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets()
                .add(getClass().getResource("/css/register.css").toExternalForm());
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.getIcons().add(iconTopLeft);

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
            controller.setUserService(userService, postService, categoryService, app);

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets()
                .add(getClass().getResource("/css/board.css").toExternalForm());

            stage.setTitle("Bulletin Board");
            stage.setScene(scene);
            stage.getIcons().add(iconTopLeft);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProfile() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/ProfileView.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setUserService(userService, postService, categoryService, app);

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets()
                .add(getClass().getResource("/css/profile.css").toExternalForm());

            stage.setTitle("Profile");
            stage.setScene(scene);
            stage.getIcons().add(iconTopLeft);

        } catch (Exception e) {
            e.printStackTrace();
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

    public void logout() {
        UserSession.logout();
        showLogin();
    }
}
