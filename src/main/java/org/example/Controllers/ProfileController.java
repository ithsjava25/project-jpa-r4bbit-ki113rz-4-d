package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.App;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.Services.UserService;
import org.example.UserSession;

import java.util.Objects;


public class ProfileController {

    private UserService userService;
    private PostService postService;
    private CategoryService categoryService;
    private App app;

    public void setUserService(
        UserService userService,
        PostService postService,
        CategoryService categoryService,
        App app
    ) {
        this.app = app;
    }

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

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            app.logout();
        }

    }
}
