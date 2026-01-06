package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.example.Entities.User;
import org.example.Services.UserService;
import org.example.UserSession;

public class SettingsController {
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
    }

    public void editUsername() {
        //TODO: edit username
    }

    public void changePassword() {
        //TODO: change password
    }

}
