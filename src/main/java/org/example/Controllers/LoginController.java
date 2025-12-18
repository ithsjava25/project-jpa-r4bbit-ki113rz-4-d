package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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


    /**
     *
     *
     */
    @FXML
    private void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        userService.login(username, password)
            .ifPresentOrElse(
                user -> {
                    UserSession.login(user);
                    switchToBulletin();
                },
                () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Failed");
                    alert.showAndWait();
                }
            );

    }
    private void switchToBulletin() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BulletinView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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



