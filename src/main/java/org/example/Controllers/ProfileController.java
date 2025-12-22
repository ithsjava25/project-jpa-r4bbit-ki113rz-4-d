package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.App;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.UserSession;

import java.util.Objects;


public class ProfileController {

    @FXML
    private ImageView profileImage;

    @FXML
    private TextArea bioTextArea;

    @FXML
    private Button saveButton;

    public void initialize() {
        Circle clip = new Circle(50, 50, 50);
        profileImage.setClip(clip);
        Image image = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/iths.png")
            )
        );
        profileImage.setImage(image);
        UserSession.getCurrentUser()
            .map(User::getProfile)
            .map(Profile::getBio)
            .ifPresentOrElse(
                bio -> bioTextArea.setText(bio),
                () -> bioTextArea.setText("Hello! Welcome to your bio, write something about yourself: ")
            );
    }
    public void testMethod() {
        App app = App.getAppInstance();
        app.showBoard();
    }

    public void editBio(ActionEvent actionEvent) {
        bioTextArea.setEditable(true);
        saveButton.setDisable(false);

    }

    public void saveBio(ActionEvent actionEvent) {
        bioTextArea.setEditable(false);
        saveButton.setDisable(true);
        //Push to db
    }
}
