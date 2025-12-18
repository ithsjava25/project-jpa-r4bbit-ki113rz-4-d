package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Services.PostService;
import org.example.Services.UserService;
import org.example.UserSession;

/**
 * Class to link LoginView to Controller
 */

public class LoginController {

    private UserService userService;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        loginButton.disableProperty().bind(
            usernameField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
        );
    }

    @FXML
    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        passwordField.clear();

        userService.login(username, password)
            .ifPresentOrElse(
                user -> {
                    UserSession.login(user);
                    App.getAppInstance().showBoard();
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

    @FXML
    private void handleCreateUser() {
        App.getAppInstance().showRegister();
    }
}



