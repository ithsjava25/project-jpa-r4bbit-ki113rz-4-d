package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.example.Entities.Post;
import org.example.Services.PostService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class PostItemController {

    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private StackPane root;
    @FXML private ImageView postItImage;

    private Post post;
    private PostService postService;
    private Runnable onPostChanged;


    @FXML void initialize() {
        actionBox.setVisible(false);

        root.setOnMouseEntered(event -> actionBox.setVisible(true));
        root.setOnMouseExited(event -> actionBox.setVisible(false));

        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());

        root.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.DELETE) {
                handleDelete();
                event.consume();
            }
        });
    }
    public void setPost(Post post) {
        this.post = post;
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());

        String colorPath = post.getPostItColor();
        if (colorPath == null || colorPath.isBlank()) {
            colorPath = "/Images/PostIt_Blue.jpg";
        }
        postItImage.setImage(new Image(colorPath));
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
        //Ta bort post-it från databas
        if (postService != null && post != null) {
            postService.deletePost(post);
        }
        //Ta bort post-it från GUI
        if (root.getParent() instanceof Pane parent) {
            parent.getChildren().remove(root);
        }
        if (onPostChanged != null) {
            onPostChanged.run();
        }
        System.out.println("Post deleted!");
    }
}
