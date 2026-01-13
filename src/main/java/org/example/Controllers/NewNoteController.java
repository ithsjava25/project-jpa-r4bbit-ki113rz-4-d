package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Services.CategoryService;
import org.example.Services.PostService;
import org.example.UserSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Controller connected to NewNote.fxml.
 *
 * Responsible for creating and editing posts through a form-based UI.
 * Handles all user interactions inside the "new / edit post" dialog,
 * such as typing in text fields, selecting categories, and clicking save or cancel.
 *
 * This controller validates user input and delegates all business logic
 * related to creating or updating posts to the PostService.
 * It does not perform any database operations directly.
 *
 * The dialog is modal and short-lived, and will close once the post
 * has been successfully saved or the action is cancelled.
 */

public class NewNoteController {

    @FXML private TextField subjectField;
    @FXML private TextArea messageArea;
    @FXML private MenuButton categoryMenu;
    @FXML private Label charCountLabel;

    private PostService postService;
    private Consumer<Post> onPostSaved;
    private CategoryService categoryService;
    private Post postToEdit;
    private boolean categoriesLoaded = false;
    private static final int MAX_LENGHT = 255;

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

    public void setOnPostSaved(Consumer<Post> onPostSaved) {
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Missing required fields");
            alert.setContentText("Please enter both subject and message.");
            alert.showAndWait();
            return;
        }

        List<Long> categoryIds = categoryItems.entrySet().stream()
            .filter(e -> e.getValue().isSelected())
            .map(Map.Entry::getKey)
            .toList();

        if (categoryIds.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("No category selected");
            alert.setContentText("Please select at least one category.");
            alert.showAndWait();
            return;
        }
        User currentUser = UserSession.getCurrentUser()
            .orElseThrow(()->new IllegalStateException("No user logged in!"));
        Post result;

        //Update existing post
        if (postToEdit != null) {
            result = postService.updatePost(
                postToEdit,
                subject,
                message,
                categoryIds,
                currentUser);
        }
        //Create new post
        else {
                result = postService.createPost(
                    subject,
                    message,
                    categoryIds,
                    currentUser);
            }
        if (onPostSaved != null) {
            onPostSaved.accept(result);
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
    @FXML
    public void initialize(){
        messageArea.setWrapText(true);

        messageArea.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= MAX_LENGHT){
                return change;
            }
            return null;
        }));

        messageArea.textProperty().addListener((obs, oldText, newText) -> {
            charCountLabel.setText(newText.length() + " / " + MAX_LENGHT);
        });
    }
}
