package org.example.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Dialog;
import org.example.App;
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/post_item.fxml"));
                Node postNode = loader.load();

                PostItemController controller = loader.getController();
                controller.setPost(post);
                postContainer.getChildren().add(postNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void newPost () {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Note");
        dialog.setHeaderText("Enter your subject and message: ");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Message");
        messageArea.setPrefRowCount(3);

        grid.add(new Label("Subject:"), 0, 0);
        grid.add(subjectField, 1, 0);
        grid.add(new Label("Message:"), 0, 1);
        grid.add(messageArea, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(subjectField::requestFocus);

        dialog.showAndWait().ifPresent(input -> {
            if (input == saveButtonType) {
                System.out.println("Save clicked!");
                Post newPost = new Post(subjectField.getText(), messageArea.getText());

                userService.getUserById(1L).ifPresent(user -> {
                    postService.createPost(newPost, user);
                    System.out.println("Post saved in database!");

                    try {
                        loadPosts();
                        System.out.println("loadPosts() has run!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
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
}
