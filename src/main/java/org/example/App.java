package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

/**
 * Use launcher to launch the App class
 * Creates a PersistenceConfiguration
 * With the PersistenceConfiguration create a EntityManagerFactory(emf)
 * Inject the Repos and Services with the EntityManagerFactory
 * Creates a fxml loader (fxmlLoader)
 * Uses the loader to load the UI inside the fxml
 * Finally sets a scene with the fxml UI
 */

public class App extends Application {
    private EntityManagerFactory emf;

    @Override
    public void start(Stage stage) throws Exception {
        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/bulletin")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(User.class);

        emf = cfg.createEntityManagerFactory();

        //Repositories
        UserRepositoryImpl userRepo = new UserRepositoryImpl(emf);

        //Services
        UserService userService = new UserServiceImpl(userRepo);

        //Load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/BulletinView.fxml"));
        Parent root = fxmlLoader.load();

        //Inject userService
        Controller controller = fxmlLoader.getController();
        controller.setUserService(userService);

        //Show stage
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        if (emf != null) {
            emf.close();
        }
    }
}
