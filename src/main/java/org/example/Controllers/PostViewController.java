package org.example.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.Entities.Category;
import org.example.Entities.Post;
import java.time.format.DateTimeFormatter;

public class PostViewController {
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private Label metaLabel;
    @FXML private FlowPane categoryPane;
    @FXML private BorderPane root;

    private Post post;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setPost (Post post) {
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());

        String metaText =
            "By " + post.getAuthor().getUsername() +
                " • " + post.getCreatedAt().format(formatter);

        if (post.getUpdatedAt() != null && post.getUpdatedBy() != null) {
            metaText += "\nUpdated by " +
                post.getUpdatedBy().getUsername() +
                " • " + post.getUpdatedAt().format(formatter);
        }

        metaLabel.setText(metaText);

        root.setStyle(
            "-fx-background-image: url('" + post.getPostItColor() + "');" +
            "-fx-background-size: contain;" +
            "-fx-background-repeat: no-repeat;" +
            "-fx-background-position: center;"
        );

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
        setPostItBackground(post.getPostItColor());
    }
    @FXML
    private void handleClose() {
        ((Stage) subjectLabel.getScene().getWindow()).close();
    }

    private void setPostItBackground(String imagePath) {
        BackgroundImage bg = new BackgroundImage(
            new Image(imagePath),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(
                100, 100,
                true,
                true,
                false,
                true
            )
        );
        root.setBackground(new Background(bg));
    }
}
