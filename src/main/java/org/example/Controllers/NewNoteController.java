package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.UserSession;

public class NewNoteController {

    @FXML private TextField subjectField;
    @FXML private TextArea messageArea;
    @FXML private ComboBox<Category> categoryBox;

    private PostService postService;
    private CategoryService categoryService;

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        categoryBox.getItems().setAll(categoryService.getAllCategories());
    }

//Save new note
    @FXML
    private void handleSave() {
        User author = UserSession.getCurrentUser()
            .orElseThrow(() -> new IllegalStateException("No user logged in!"));

        Post post = new Post();
        post.setSubject(subjectField.getText());
        post.setMessage(messageArea.getText());
        post.setAuthor(author);

        Category selectedCategory = categoryBox.getValue();
        if (selectedCategory != null){
            post.addCategory(selectedCategory);
        }

        postService.createPost(post, author);

        closeWindow();
    }

    //Cancel
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    public void closeWindow() {
        Stage stage = (Stage) subjectField.getScene().getWindow();
        stage.close();
    }
}
