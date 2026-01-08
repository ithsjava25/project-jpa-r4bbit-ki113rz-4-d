package org.example.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.example.Entities.Category;
import org.example.Entities.Post;
import java.time.format.DateTimeFormatter;

public class PostViewController {
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private Label metaLabel;
    @FXML private FlowPane categoryPane;

    private Post post;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setPost (Post post) {
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());

        metaLabel.setText(
            "By" + post.getAuthor().getUsername() +
                "•" + post.getCreatedAt().format(formatter)
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
    }
    @FXML
    private void handleClose() {
        ((Stage) subjectLabel.getScene().getWindow()).close();
    }
}
