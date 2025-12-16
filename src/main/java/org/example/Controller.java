package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.fxml.FXML;
import nonapi.io.github.classgraph.json.JSONUtils;

import java.awt.event.ActionEvent;
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

    @FXML
    private FlowPane postContainer;

    public Controller() {
    }

    public void setUserService(UserService userService, PostService postService) {
        if (userService == null || postService == null) {
            throw new IllegalArgumentException("Services cannot be null");
        }

        this.userService = userService;
        this.postService = postService;
        try {
            loadPosts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
        //Get info from javaFX
        //Login logic here
    }

    public void registerUser() {
        userService.createUser("Button", "Clicked", "secret")
            .ifPresentOrElse(
                user -> {
                    //What happens if successfully created user
                    System.out.println("User created: " + user.getUsername());
                },
                () -> {
                    //What happens if failed creating user
                    System.out.println("User already exists or invalid input");
                }
            );
    }

    @FXML
    private void handleUsers() {
        System.out.println("Handle users clicked!");
    }

    @FXML
    private void initialize() {

    }

    public void loadPosts() throws IOException {
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
    private void newPost() throws IOException {
        System.out.println("New post clicked!");
        loadPosts();
    }
}
