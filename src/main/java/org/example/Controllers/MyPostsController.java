package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.Entities.User;
import org.example.Services.PostService;
import org.example.UserSession;

import java.util.Optional;

public class MyPostsController {

    @FXML
    private VBox postsContainer;

    private PostService postService;

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public void loadMyPosts() {
        Optional<User> currentUserOpt = UserSession.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return;
        }

        User currentUser = currentUserOpt.get();

        postService.getPostsByUser(currentUser).forEach(post -> {
            Label label = new Label(post.getSubject());
            postsContainer.getChildren().add(label);
        });
    }
}
