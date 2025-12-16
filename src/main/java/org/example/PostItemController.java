package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PostItemController {

    @FXML private HBox actionBox;
    @FXML private Label subjectLabel;
    @FXML private Label messageLabel;

    @FXML void initialize() {
        actionBox.setVisible(false);
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
