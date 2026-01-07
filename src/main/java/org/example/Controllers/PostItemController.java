package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Entities.Post;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class PostItemController {
    @FXML private StackPane root;
    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private ImageView postItImage;
    @FXML private Label authorLabel;
    @FXML private Label createdAtLabel;

    private Post post;
    private CategoryService categoryService;
    private PostService postService;
    private Runnable onPostChanged;


    @FXML void initialize() {
        actionBox.setVisible(false);

        root.setOnMouseEntered(e -> actionBox.setVisible(true));
        root.setOnMouseExited(e -> actionBox.setVisible(false));
    }

    public void setPost(Post post) {
        this.post = post;
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());

        postItImage.setImage(new Image(post.getPostItColor()));

        if (post.getAuthor() != null) {
            authorLabel.setText("Author: " + post.getAuthor().getUsername());
        } else {
            authorLabel.setText("");
        }
        if (post.getCreatedAt() != null) {
            DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            createdAtLabel.setText(post.getCreatedAt().format(formatter));
        } else {
            createdAtLabel.setText("");
        }
    }

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setOnPostChanged(Runnable onPostChanged) {
        this.onPostChanged = onPostChanged;
    }

    // ==== Button handlers ====
    @FXML public void handleUpdate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewNote.fxml"));
            Parent root = loader.load();

            NewNoteController controller = loader.getController();
            controller.setPostService(postService);
            controller.setCategoryService(categoryService);
            controller.setPostToEdit(post);
            controller.setOnPostSaved(onPostChanged);

            Stage stage = new Stage();
            stage.setTitle("Update post");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML public void handleDelete() {
        //Ta bort post-it från databas
        if (postService != null && post != null) {
            postService.deletePost(post);
            System.out.println("Post deleted!");
        }
        if (onPostChanged != null) {
            onPostChanged.run();
        }
        //Ta bort post-it från GUI
        if (root.getParent() instanceof Pane parent) {
            parent.getChildren().remove(root);
        }
    }

    @FXML
    private void handleShow() {
        // EJ KLART
        System.out.println("Show message: " + post.getSubject());
    }
}
