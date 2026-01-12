package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.UserSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller connected to the PostItem.fxml.
 *
 * Represents a single post-it note displayed on the bulletin board.
 * Responsible for rendering a summarized view of a post, including
 * subject, message preview, author, categories, and post-it appearance.
 *
 * Handles user interactions such as hover actions, edit and delete buttons,
 * and opening the post in a full-screen view.
 *
 * This controller communicates with PostService when a post needs to be
 * updated or deleted, but does not contain any business or persistence logic itself.
 */

public class PostItemController {
    @FXML private StackPane root;
    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private ImageView postItImage;
    @FXML private Label authorLabel;
    @FXML private Label createdAtLabel;
    @FXML private FlowPane categoryPane;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Label updatedLabel;

    private Post post;
    private CategoryService categoryService;
    private PostService postService;
    private Runnable onPostChanged;


    @FXML void initialize() {
        actionBox.setVisible(false);

        root.setOnMouseEntered(e -> actionBox.setVisible(true));
        root.setOnMouseExited(e -> actionBox.setVisible(false));


        Tooltip.install(editButton, new Tooltip("Update Post"));
        Tooltip.install(deleteButton, new Tooltip("Delete Post"));

        Tooltip editTip = new Tooltip("Update Post");
        Tooltip deleteTip = new Tooltip("Delete Post");

        root.setOnMouseClicked(e-> {
            if (e.getTarget() instanceof Button) {
                return;
            }
        handleShow();
        });
    }

    private void renderCategories() {
        categoryPane.getChildren().clear();
        for (Category c : post.getCategories()) {
            Label tag = new Label(c.getName());
            tag.setStyle("""
                    -fx-background-color: rgba(0,0,0,0.1);
                    -fx-padding: 3 6;
                    -fx-background-radius: 10;
                    -fx-font-size: 10;
                """);
            categoryPane.getChildren().add(tag);
        }
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
        updatedLabel.setVisible(post.getUpdatedAt() != null);
        renderCategories();
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
        if(!isCurrentUser(post)){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewNote.fxml"));
            Parent root = loader.load();

            NewNoteController controller = loader.getController();
            controller.setPostService(postService);
            controller.setCategoryService(categoryService);
            controller.setPostToEdit(post);

            controller.setOnPostSaved(updatedPost -> {
                this.post = updatedPost;
                setPost(updatedPost);

                if (onPostChanged != null) {
                    onPostChanged.run();
                }
            });

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

    @FXML public void handleDelete() {
        //Ta bort post-it från databas

        if(!isCurrentUser(post)){
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete post");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("This post will be permanently deleted.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK && postService != null && post != null) {
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PostView.fxml"));
            Parent root = loader.load();

            PostViewController controller = loader.getController();
            controller.setPost(post);

            Stage stage = new Stage();
            stage.setTitle("Show post");
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root, 800, 800);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to open show dialog");
            alert.setContentText("Could not load the post view: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public boolean isCurrentUser(Post post) {
        User currentUser = UserSession.getCurrentUser().orElseThrow();
        if(!post.getAuthor().getUserId().equals(currentUser.getUserId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong user");
            alert.setHeaderText("Not your post!");
            alert.setContentText("You do not have permission to do that!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
