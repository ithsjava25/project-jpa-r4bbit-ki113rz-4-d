package org.example;
//TODO: Login, registration
//TODO: Display posts, create posts, delete/edit posts
//TODO: Filter by category, assign categories when posting
public class Controller {
    private UserService userService;

    public Controller() {
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
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
