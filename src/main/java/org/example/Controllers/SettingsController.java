package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.example.Entities.User;
import org.example.Services.UserService;
import org.example.UserSession;

public class SettingsController {
    @FXML
    private Button cancelPasswordButton;
    @FXML
    private Button cancelUsernameButton;
    @FXML
    private Button cancelNameButton;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField oldPasswordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Button savePasswordButton;
    @FXML
    private TextField usernameTextfield;
    @FXML
    private Button saveUsernameButton;
    @FXML
    private HBox nameEditBox;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private Button saveNameButton;
    @FXML
    private Label nameLabel;

    @FXML
    private Label usernameLabel;
    private UserService userService;

    private String originalUsername;

    public void loadSettings() {
        UserSession.getCurrentUser().ifPresentOrElse(
            user -> {
                nameLabel.setText(user.getFirst_name() + " " + user.getLast_name());
                usernameLabel.setText(user.getUsername());
            },
                    () -> {
                        throw new IllegalStateException("Not logged on");
                    });
    }

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @FXML
    private void editName() {
        User user = UserSession.getCurrentUser().orElseThrow();

        firstNameField.setText(user.getFirst_name());
        lastNameField.setText(user.getLast_name());

        nameLabel.setVisible(false);
        nameEditBox.setVisible(true);
        saveNameButton.setVisible(true);;
        cancelNameButton.setVisible(true);
    }

    @FXML
    private void saveName() {
        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();

        if (first.isBlank() || last.isBlank()) return;

        User user = UserSession.getCurrentUser().orElseThrow();
        userService.updateName(user, first, last);

        nameLabel.setText(first + " " + last);
        nameLabel.setVisible(true);
        nameEditBox.setVisible(false);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
    }

    public void editUsername() {
        User user = UserSession.getCurrentUser().orElseThrow();
        originalUsername = user.getUsername();
        usernameTextfield.setText(originalUsername);
        usernameLabel.setVisible(false);
        usernameTextfield.setVisible(true);
        saveUsernameButton.setVisible(true);
        cancelUsernameButton.setVisible(true);
    }

    public void saveUsername() {
        User user = UserSession.getCurrentUser().orElseThrow();
        String newUsername = usernameTextfield.getText();

        if (newUsername.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Invalid username", "Username cannot be empty");
            return;
        }

        if (userService.getUserByUsername(newUsername).isPresent()){
            showAlert(
                Alert.AlertType.ERROR,
                "Username already exists",
                "That username is taken."
            );
            return;
        }
        userService.updateUsername(user, newUsername);
        usernameLabel.setText(newUsername);
        usernameLabel.setVisible(true);
        usernameTextfield.setVisible(false);
        saveUsernameButton.setVisible(false);
        cancelUsernameButton.setVisible(false);
    }

    public void cancelUsernameEdit() {
        usernameTextfield.setText(originalUsername);
        usernameLabel.setVisible(true);
        usernameTextfield.setVisible(false);
        saveUsernameButton.setVisible(false);
        cancelUsernameButton.setVisible(false);
    }

    public void cancelNameEdit() {
        nameLabel.setVisible(true);
        nameEditBox.setVisible(false);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
    }

    public void cancelPasswordEdit() {
        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        oldPasswordField.setVisible(false);
        confirmPasswordField.setVisible(false);
        newPasswordField.setVisible(false);
        savePasswordButton.setVisible(false);
        cancelPasswordButton.setVisible(false);
    }

    public void changePassword() {
        oldPasswordField.setVisible(true);
        confirmPasswordField.setVisible(true);
        newPasswordField.setVisible(true);
        savePasswordButton.setVisible(true);
        cancelPasswordButton.setVisible(true);
    }


    public void savePassword() {
        User user = UserSession.getCurrentUser().orElseThrow();
        String username = user.getUsername();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            showAlert(
                Alert.AlertType.WARNING,
                "Missing information",
                "All password fields must be filled in."
            );
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showAlert(
                Alert.AlertType.ERROR,
                "Password mismatch",
                "New passwords must match."
            );
            return;
        }

        if (userService.updatePassword(username, oldPassword, newPassword, confirmPassword)){
            showAlert(
                Alert.AlertType.INFORMATION,
                "Password updated",
                "Your password has been successfully changed."
            );
            oldPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
            oldPasswordField.setVisible(false);
            confirmPasswordField.setVisible(false);
            newPasswordField.setVisible(false);
            savePasswordButton.setVisible(false);
        } else {
            showAlert(
                Alert.AlertType.ERROR,
                "Update failed",
                "Your old password is incorrect."
            );
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
