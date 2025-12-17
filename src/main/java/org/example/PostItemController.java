package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class PostItemController {

    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;
    @FXML private StackPane root;

    @FXML void initialize() {
        actionBox.setVisible(false);

        root.setOnMouseEntered(event -> actionBox.setVisible(true));
        root.setOnMouseExited(event -> actionBox.setVisible(false));
    }

    @FXML public void handleUpdate() {
        System.out.println("Update post");
    }

    @FXML public void handleDelete() {
        System.out.println("Delete post");
    }

    public void setPost(Post post) {
        subjectLabel.setText(post.getSubject());
        messageLabel.setText(post.getMessage());
    }
}
