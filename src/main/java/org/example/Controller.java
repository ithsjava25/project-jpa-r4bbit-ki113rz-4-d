package org.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Dialog;
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

    public Controller(UserService userService, PostService postService) {}
    public Controller() {
        }

        public void setUserService (UserService userService, PostService postService){
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

        @FXML
        private void initialize () {

        }


        public void login (String username, String password){
            //Get info from javaFX
            //Login logic here
        }

        public void registerUser () {
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

}
