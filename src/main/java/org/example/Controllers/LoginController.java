package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;

/**
 * Class to link LoginView to Controller
 */

public class LoginController {

    private UserService userService;
    private PostService postService;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @FXML
    private void handleLogin() throws IOException {

        //TODO Kontrollera anv+lösenord


        FXMLLoader loader = new FXMLLoader(getClass().getResource("BulletinView.fxml"));
        Parent root = loader.load();

        //Fetch Bulletin-Controller
        Controller bulletinController = loader.getController();
        bulletinController.setUserService(userService, postService);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/board.css").toExternalForm());

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Bulletin Board");
        stage.setScene(scene);
    }
}
