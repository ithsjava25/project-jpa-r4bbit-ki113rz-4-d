package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.UserSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewNoteController {

    @FXML private TextField subjectField;
    @FXML private TextArea messageArea;
    @FXML private MenuButton categoryMenu;

    private final Map<Long, CheckMenuItem> categoryItems = new HashMap<>();

    private PostService postService;
    private Runnable onPostSaved;
    private CategoryService categoryService;

    public void initialize(){
        loadCategories();
    }

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
        if (categoryService==null || categoryMenu == null)  return;

        categoryMenu.getItems().clear();
        categoryItems.clear();

        for (Category c : categoryService.getAllCategories()) {
            CheckMenuItem item = new CheckMenuItem(c.getName());
            categoryMenu.getItems().add(item);
            categoryItems.put(c.getCategoryId(), item);
        }
    }


//Save new note
    @FXML
    private void handleSave() {
        String subject = subjectField.getText();
        String message = messageArea.getText();

        User author = UserSession.getCurrentUser()
            .orElseThrow(() -> new IllegalStateException("No user logged in"));

        if (subject == null || subject.isBlank()
            || message == null || message.isBlank()) {
            return;
        }

        List<Long> categoryIds = categoryItems.entrySet().stream()
            .filter(e -> e.getValue().isSelected())
            .map(Map.Entry::getKey)
            .toList();

        if (categoryIds.isEmpty())
            return;

        //Save through service
        postService.createPost(subject, message, categoryIds, author);

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
