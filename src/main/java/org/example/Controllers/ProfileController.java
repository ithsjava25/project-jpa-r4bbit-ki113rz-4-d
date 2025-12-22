package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.App;

import java.util.Objects;


public class ProfileController {

    @FXML
    private ImageView profileImage;

    public void initialize() {
        Circle clip = new Circle(50, 50, 50);
        profileImage.setClip(clip);
        Image image = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/iths.png")
            )
        );
        profileImage.setImage(image);
    }
    public void testMethod() {
        App app = App.getAppInstance();
        app.showBoard();
    }
}
