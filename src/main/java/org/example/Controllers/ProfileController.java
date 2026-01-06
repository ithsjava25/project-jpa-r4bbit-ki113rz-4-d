package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import org.example.App;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.Services.UserService;
import org.example.UserSession;

import java.io.IOException;
import java.util.Objects;


public class ProfileController {

    private UserService userService;
    private PostService postService;
    private CategoryService categoryService;
    private App app;


    @FXML
    private ImageView profileImage;

    @FXML
    private TextArea bioTextArea;

    @FXML
    private Button saveButton;

    @FXML
    private Button editButton;

    @FXML
    private BorderPane contentPane;


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

    public void setUserService(UserService userService, PostService postService, CategoryService categoryService, App app
    ) {
        this.app = app;
        this.userService = userService;
        this.postService = postService;
        showMyPosts();
    }
    public void testMethod() {
        App app = App.getAppInstance();
        app.showBoard();
    }

    public void editBio(ActionEvent actionEvent) {
        bioTextArea.setEditable(true);
        saveButton.setDisable(false);
        editButton.setDisable(true);
    }

    public void saveBio(ActionEvent actionEvent) {
        bioTextArea.setEditable(false);
        saveButton.setDisable(true);
        editButton.setDisable(false);

        String newBio = bioTextArea.getText();
        UserSession.getCurrentUser().ifPresentOrElse(
            user -> userService.updateBio(user, newBio),
            () -> {
                throw new IllegalStateException("No user logged in");
            }
        );

        //Push to db
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

    public void showMyPosts() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/myPosts.fxml"));
            Parent myPostsView = loader.load();

            MyPostsController controller = loader.getController();
            controller.setPostService(postService);
            controller.loadMyPosts();
            contentPane.setCenter(myPostsView);

        } catch (IOException e) {
            throw new  RuntimeException(e);
        }
    }

    public void showSettings() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/userSettings.fxml"));
            Parent settingsView = loader.load();

            SettingsController controller = loader.getController();
            controller.loadSettings();
            contentPane.setCenter(settingsView);

        } catch (IOException e) {
            throw new  RuntimeException(e);
        }
    }
}
