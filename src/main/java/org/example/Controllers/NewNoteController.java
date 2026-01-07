package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;
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
    @FXML private Button saveButton;

    private PostService postService;
    private Runnable onPostSaved;
    private CategoryService categoryService;
    private Post postToEdit;
    private boolean categoriesLoaded = false;

    private final Map<Long, CheckMenuItem> categoryItems = new HashMap<>();

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        loadCategories();
        categoriesLoaded = true;

        if (postToEdit != null) {
            selectPostCategories();
        }
    }

    public void setOnPostSaved(Runnable onPostSaved) {
        this.onPostSaved = onPostSaved;
    }

//===LOAD CATEGORIES===
    private void loadCategories() {
        if (categoryService==null || categoryMenu == null)  return;

        categoryMenu.getItems().clear();
        categoryItems.clear();

        for (Category c : categoryService.getAllCategories()) {
            CheckMenuItem item = new CheckMenuItem(c.getName());
            item.setOnAction(e -> updateMenuText());

            categoryMenu.getItems().add(item);
            categoryItems.put(c.getCategoryId(), item);
        }
        updateMenuText();
    }

    private void selectPostCategories() {
        if (postToEdit == null) return;

        for (Category c : postToEdit.getCategories()) {
            CheckMenuItem item = categoryItems.get(c.getCategoryId());
            if (item != null) {
                item.setSelected(true);
            }
        }
        updateMenuText();
    }

    private void updateMenuText() {
        List <String> selected = categoryItems.values().stream()
            .filter(CheckMenuItem::isSelected)
            .map(CheckMenuItem::getText)
            .toList();

        if (selected.isEmpty()) {
            categoryMenu.setText("Category..");
        } else {
            categoryMenu.setText(String.join(", ", selected));
        }
    }

// ===EDIT-MODE===
    public void setPostToEdit(Post post){
        this.postToEdit = post;

        subjectField.setText(post.getSubject());
        messageArea.setText(post.getMessage());

        if (categoriesLoaded) {
            selectPostCategories();
        }
    }

// ===Save===
    @FXML
    private void handleSave() {
        String subject = subjectField.getText();
        String message = messageArea.getText();

        if (subject == null || subject.isBlank()
            || message == null || message.isBlank()) {
            System.out.println("Subject or Message is null or blank!");
            return;
        }

        List<Long> categoryIds = categoryItems.entrySet().stream()
            .filter(e -> e.getValue().isSelected())
            .map(Map.Entry::getKey)
            .toList();

        if (categoryIds.isEmpty()) {
            System.out.println("No category selected!");
            return;
        }

        //Update existing post
        if (postToEdit != null) {
            postService.updatePost(
                postToEdit,
                subject,
                message,
                categoryIds);
        }
        //Create new post
        else {
            User author = UserSession.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("No user logged in"));

                postService.createPost(
                    subject,
                    message,
                    categoryIds,
                    author
                );
            }
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
