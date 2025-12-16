package org.example;

import jakarta.persistence.*;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String first_name;
    private String last_name;
    private String username;

    public User(String firstName, String lastName, String username, String password) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.username = username;
        this.password = password;
    }

    public User() {}

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", first_name='" + first_name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

}
