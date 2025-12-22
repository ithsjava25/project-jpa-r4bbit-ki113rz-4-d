package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.example.Entities.Post;
import org.example.Services.PostService;

public class PostItemController {

    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private StackPane root;

    private Post post;
    private PostService postService;
    private Runnable onPostChanged;

    @FXML void initialize() {
        actionBox.setVisible(false);

        root.setOnMouseEntered(event -> actionBox.setVisible(true));
        root.setOnMouseExited(event -> actionBox.setVisible(false));
    }
    public void setPost(Post post) {
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());
    }
    public void setPostService(PostService postService) {
        this.postService = postService;
    }
    public void setOnPostChanged(Runnable onPostChanged) {
        this.onPostChanged = onPostChanged;
    }

    @FXML public void handleUpdate() {
        System.out.println("Update post");
    }

    @FXML public void handleDelete() {
        postService.deletePost(post);
        onPostChanged.run();
        System.out.println("Delete post");
    }


}
