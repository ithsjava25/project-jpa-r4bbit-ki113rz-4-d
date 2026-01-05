package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Dialog;
import org.example.App;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Entities.Post;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.Services.UserService;
import org.example.UserSession;

import java.io.IOException;
import java.util.List;


/**
 * This is connected to the fxml file.
 * This class will control everything the user does inside the app
 * Button clicks, typing in text fields, showing the bulletin board
 * posting stuff etc.
 * Handles login
 * When needed, it will "talk" the service classes (UserService, PostService)
 * the service classes will handle anything database related
 * (If needed, we can create a LoginController, UserController and a PostController)
 *
 */
//TODO: Login, registration
//TODO: Display posts, create posts, delete/edit posts
//TODO: Filter by category, assign categories when posting
public class Controller {
    private UserService userService;
    private PostService postService;
    private CategoryService categoryService;
    private App app;

    @FXML
    private FlowPane postContainer;


    public void setUserService (UserService userService, PostService postService, CategoryService categoryService, App app){
        if (userService == null || postService == null || categoryService == null || app == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.userService = userService;
        this.postService = postService;
        this.categoryService = categoryService;
        this.app = app;
        try {
            loadPosts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize () {

    }

    @FXML
    private void handleUsers () {
        System.out.println("Handle users clicked!");
    }


    public void loadPosts () throws IOException {
        postContainer.getChildren().clear();
        List<Post> posts = postService.getAllPosts();

        for (Post post : posts) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/post_item.fxml"));

            Node postNode = loader.load();

            PostItemController controller = loader.getController();
            controller.setPost(post);
            controller.setPostService(postService);
            controller.setOnPostChanged(() -> {
                try {
                    loadPosts();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            postContainer.getChildren().add(postNode);
        }
    }

    private void reloadAfterSave() {
        try {
            loadPosts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void newPost () {

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/NewNote.fxml"));

            Parent root = loader.load();

            NewNoteController controller = loader.getController();
            controller.setPostService(postService);
            controller.setCategoryService(categoryService);
            controller.setOnPostSaved(() -> {
                try {
                    loadPosts();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                });

            Stage stage = new Stage();
            stage.setTitle("New Note");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleProfile() {
        app.showProfile();
//        UserSession
//            .getCurrentUser()
//            .map(User::getProfile)
//            .map(Profile::getBio)
//            .ifPresentOrElse(
//                System.out::println,
//                () -> System.out.println("No profile found!")
//            );
//        System.out.println();
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            UserSession.logout();
            app.showLogin();
        }

    }
}
