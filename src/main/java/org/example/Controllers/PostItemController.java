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
import java.util.List;
import java.util.Random;

public class PostItemController {

    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private StackPane root;
    @FXML private ImageView postItImage;

    private Post post;
    private PostService postService;
    private Runnable onPostChanged;

    private static final List<String> POSTIT_IMAGES = List.of(
        "/Images/PostIt_Yellow.jpg",
        "/Images/PostIt_Blue.jpg",
        "/Images/PostIt_LightGreen.jpg",
        "/Images/PostIt_Pink.jpg",
        "/Images/PostIt_Purple.jpg"
    );

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
        applyRandomPostItColor();
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

    private void applyRandomPostItColor() {
        String path = POSTIT_IMAGES.get(new Random().nextInt(POSTIT_IMAGES.size()));
        postItImage.setImage(new Image(path));
    }
}
