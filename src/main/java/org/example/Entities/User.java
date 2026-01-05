package org.example.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user_account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "user"
        , cascade = CascadeType.ALL
        , orphanRemoval = true)
    private Profile profile;

    public User(String firstName, String lastName, String username, String password) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.username = username;
        this.password = password;
    }

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", first_name='" + first_name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", username='" + username + '\'' +
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

    public List<Post> getPosts() {
        return posts;
    }

    /* ===== Relation helper ===== */
    public void addPost(Post post) {
        if (post == null) return;
        posts.add(post);
        post.setAuthor(this);
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this);
        }
    }

    public Profile getProfile() {
        return profile;
    }

}
