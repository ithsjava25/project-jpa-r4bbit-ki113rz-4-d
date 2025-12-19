package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Services.UserService;

public class RegisterController {

    private UserService userService;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        registerButton.disableProperty().bind(
            firstNameField.textProperty().isEmpty()
                .or(lastNameField.textProperty().isEmpty())
                .or(usernameField.textProperty().isEmpty())
                .or(passwordField.textProperty().isEmpty())
        );
    }

    @FXML
    private void handleRegister() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        userService.createUser(firstName, lastName, password, username)
            .ifPresentOrElse(
                user -> {
                    App.getAppInstance().showLogin();
                },
                () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Registration Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("User could not be created. Make sure all fields are filled and username is unique.");
                    alert.showAndWait();
                    passwordField.clear();
                }
            );
    }

    private void registrationFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Failed");
        alert.setHeaderText(null);
        alert.setContentText("Registration Failed");
        alert.showAndWait();
    }

    @FXML
    private void showLogin() {
        App.getAppInstance().showLogin();
    }
}
