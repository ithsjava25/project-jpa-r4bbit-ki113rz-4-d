package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.UserSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This class controls the My post view inside the settings view.
 * It gets all the current user sessions posts from the database
 * and displays them in order by created_at time
 */

public class MyPostsController {

    @FXML
    private VBox postsContainer;

    private PostService postService;
    private CategoryService categoryService;

    /**
     * Injects postService
     * loads the posts into the view
     * @param postService
     */
    public void setPostService(PostService postService) {
        this.postService = postService;
        loadMyPosts();
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    /**
     * Gets the current user session and gets all posts for that user from database
     * it then adds each post into the postsContainer that is loaded into the view
     */
    public void loadMyPosts() {
        postsContainer.getChildren().clear();
        Optional<User> currentUserOpt = UserSession.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return;
        }

        User currentUser = currentUserOpt.get();

        List<Post> posts = postService.getPostsByUser(currentUser);
        for (Post post : posts) {
            postsContainer.getChildren().add(createPostRow(post));
        }
    }

    /**
     * helper to create a nice looking row of my posts
     * @param post current post
     * @return
     */
    private HBox createPostRow(Post post) {

        Label subject = new Label(post.getSubject());
        subject.setStyle("-fx-font-weight: bold;");

        Label message = new Label(post.getMessage());
        message.setWrapText(true);

        VBox textBox = new VBox(4, subject, message);
        textBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Button showPostButton = new Button("Show");
        showPostButton.setMinWidth(80);
        showPostButton.setPrefWidth(80);
        showPostButton.setOnAction(e -> {
            handleShowPostButton(post);
        });

        Button updateButton = new Button("Edit");
        updateButton.setMinWidth(80);
        updateButton.setPrefWidth(80);
        updateButton.setOnAction(e -> {
            handleUpdateButton(post);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-text-fill: darkred;");
        deleteButton.setMinWidth(80);
        deleteButton.setPrefWidth(80);
        deleteButton.setOnAction(e -> {
            handleDeleteButton(post);
        });

        HBox row = new HBox(10, textBox,showPostButton, updateButton, deleteButton);
        row.setStyle("""
            -fx-padding: 10;
            -fx-background-color: #f7f3ea;
            -fx-border-color: #d6c7a1;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
        """);

        return row;
    }

    private void handleShowPostButton(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PostView.fxml"));
            Parent root = loader.load();
            PostViewController controller = loader.getController();
            controller.setPost(post);

            Stage stage = new Stage();
            stage.setTitle("Show post");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 800, 800));
            stage.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to open edit dialog");
            alert.setContentText("Could not load the edit form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * when edit post button is pressed
     * opens an edit window for editing current post
     * @param post current post
     */
    private void handleUpdateButton(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewNote.fxml"));
            Parent root = loader.load();

            NewNoteController controller = loader.getController();
            controller.setPostService(postService);
            controller.setCategoryService(categoryService);
            controller.setPostToEdit(post);
            controller.setOnPostSaved(p -> loadMyPosts());

            Stage stage = new Stage();
            stage.setTitle("Update post");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to open edit dialog");
            alert.setContentText("Could not load the edit form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * When delete post button is pressed
     * deletes the current post
     * @param post current post
     */
    private void handleDeleteButton(Post post) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete post");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("This post will be permanently deleted.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            postService.deletePost(post);
            loadMyPosts();
        }
    }
}
