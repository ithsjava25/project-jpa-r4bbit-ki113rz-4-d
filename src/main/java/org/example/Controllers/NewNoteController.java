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
    private Runnable onPostSaved;
    private CategoryService categoryService;

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        loadCategories();
    }

    public void setOnPostSaved(Runnable onPostSaved) {
        this.onPostSaved = onPostSaved;
    }

    private void loadCategories() {
        if (categoryService!=null) {
            categoryBox.getItems().setAll(categoryService.getAllCategories());
        }
    }


//Save new note
    @FXML
    private void handleSave() {
        String subject = subjectField.getText();
        String message = messageArea.getText();
        Category category = categoryBox.getValue();

        User author = UserSession.getCurrentUser()
            .orElseThrow(() -> new IllegalStateException("No user logged in"));

        if (subject == null || subject.isBlank()
            || message == null || message.isBlank()
            || category == null) {
            return;
        }

        //Save through service
        postService.createPost(subject, message, category.getCategoryId(), author);

        if (onPostSaved != null) {
            onPostSaved.run();
        }

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
