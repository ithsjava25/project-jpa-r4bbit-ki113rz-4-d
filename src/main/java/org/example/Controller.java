package org.example;

/**
 * This is connected to the fxml file.
 * This class will control everything the user does inside the app
 * Button clicks, typing in text fields, showing the bulletin board
 * posting stuff etc.
 * Handles login
 * When needed, it will "talk" the service classes (UserService, PostService)
 * the service classes will handle anything database related
 * (If needed, we can create a LoginController, UserController and a PostController)
 *
 */
//TODO: Login, registration
//TODO: Display posts, create posts, delete/edit posts
//TODO: Filter by category, assign categories when posting
public class Controller {
    private UserService userService;
    private PostService postService;

    public Controller() {
    }
    public void setUserService(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    public void login(String username, String password) {
        //Get info from javaFX
        //Login logic here
    }

    public void registerUser() {
        //Get info from javaFX
        User newUser = userService.createUser("Button", "Clicked", "secret");
        System.out.println(newUser);
    }


}
