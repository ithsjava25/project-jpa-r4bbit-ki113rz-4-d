package org.example.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.Entities.Category;
import org.example.Entities.Post;
import java.net.URL;
import java.time.format.DateTimeFormatter;

/**
 * Controller connected to PostView.fxml.
 *
 * Responsible for displaying a single post in a full-screen or detailed view.
 * Shows the complete content of the post, including subject, message,
 * author information, timestamps, and categories.
 *
 * This controller is strictly read-only and does not allow editing or
 * deleting posts. It focuses solely on presentation and layout.
 *
 * The view is typically opened from a PostItemController and uses
 * the provided Post object to render all visual elements.
 */

public class PostViewController {
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private Label metaLabel;
    @FXML private FlowPane categoryPane;
    @FXML private StackPane root;
    @FXML ImageView backgroundImage;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setPost (Post post) {
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());

        String metaText = "";

        if (post.getAuthor() != null && post.getCreatedAt() != null) {
            metaText =
                "By " + post.getAuthor().getUsername() + " • " + post.getCreatedAt().format(formatter);
        }

        if (post.getUpdatedAt() != null && post.getUpdatedBy() != null) {
            metaText += "\nUpdated by " +
                post.getUpdatedBy().getUsername() +
                " • " + post.getUpdatedAt().format(formatter);
        }

        metaLabel.setText(metaText);
        setPostItBackground(post.getPostItColor());

        categoryPane.getChildren().clear();

        for (Category c : post.getCategories()) {
            Label tag = new Label(c.getName());
            tag.setStyle("""

                -fx-background-color: rgba(0,0,0,0.15);
                -fx-padding: 4 10;
                -fx-background-radius: 12;
                -fx-font-size: 12;
                """);
            categoryPane.getChildren().add(tag);
        }
    }
    @FXML
    private void handleClose() {
        ((Stage) subjectLabel.getScene().getWindow()).close();
    }

    private void setPostItBackground(String imagePath) {
        if (imagePath.startsWith("/Images/")) {
            imagePath = imagePath.replace("/Images/", "/images/");
        }

        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            System.out.println("Image not found " + imagePath);
            return;
        }

        Image image = new Image(imageUrl.toExternalForm());

        BackgroundImage bg = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(
                1.0, 1.0,
                true,
                true,
                false,
                true
            )
        );
        backgroundImage.setImage(image);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitWidth(root.getWidth());
        backgroundImage.setFitHeight(root.getHeight());

        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
    }
}
