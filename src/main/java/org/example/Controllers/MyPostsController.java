package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Services.PostService;
import org.example.UserSession;

import java.util.List;
import java.util.Optional;

public class MyPostsController {

    @FXML
    private VBox postsContainer;

    private PostService postService;

    public void setPostService(PostService postService) {
        this.postService = postService;
        loadMyPosts();
    }


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
    private HBox createPostRow(Post post) {

        Label subject = new Label(post.getSubject());
        subject.setStyle("-fx-font-weight: bold;");

        Label message = new Label(post.getMessage());
        message.setWrapText(true);

        VBox textBox = new VBox(4, subject, message);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Button updateButton = new Button("Edit post");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-text-fill: darkred;");
        deleteButton.setOnAction(e -> {
            handleDeleteButton(post);
        });

        HBox row = new HBox(10, textBox, updateButton, deleteButton);
        row.setStyle("""
            -fx-padding: 10;
            -fx-background-color: #f7f3ea;
            -fx-border-color: #d6c7a1;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
        """);

        return row;
    }

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
