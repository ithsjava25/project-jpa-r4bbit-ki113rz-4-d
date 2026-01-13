package org.example.Controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.example.App;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Entities.Post;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.Services.UserService;
import javafx.util.Duration;
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

    public void loadPosts () throws IOException {
        postContainer.getChildren().clear();
        List<Post> posts = postService.getAllPosts();

        for (Post post : posts) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/post_item.fxml"));
            Node postNode = loader.load();

            PostItemController controller = loader.getController();
            controller.setPost(post);
            controller.setPostService(postService);
            controller.setCategoryService(categoryService);
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

    @FXML
    private void newPost () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewNote.fxml"));
            Parent root = loader.load();

            NewNoteController controller = loader.getController();
            controller.setPostService(postService);
            controller.setCategoryService(categoryService);

            controller.setOnPostSaved(post -> {
                    try {
                        FXMLLoader postLoader = new FXMLLoader(getClass().getResource("/post_item.fxml"));
                        Node newPostNode = postLoader.load();

                        PostItemController postController = postLoader.getController();
                        postController.setPost(post);
                        postController.setPostService(postService);
                        postController.setCategoryService(categoryService);
                        postController.setOnPostChanged(() -> {
                            try {
                                loadPosts();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        postContainer.getChildren().add(0, newPostNode);
                        animatePostIn(newPostNode);
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

    private void animatePostIn(Node node) {
        node.setOpacity(0);
        node.setTranslateX(-400);
        node.setTranslateY(-200);
        node.setScaleX(0.8);
        node.setScaleY(0.8);
        node.setRotate(-15);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition flyIn = new TranslateTransition(Duration.millis(800), node);
        flyIn.setFromX(node.getTranslateX());
        flyIn.setFromY(node.getTranslateY());
        flyIn.setToX(0);
        flyIn.setToY(0);
        flyIn.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);

        RotateTransition rotate = new RotateTransition(Duration.millis(500), node);
        rotate.setFromAngle(-15);
        rotate.setToAngle(0);
        rotate.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fadeIn, flyIn, scale, rotate).play();
    }
}
