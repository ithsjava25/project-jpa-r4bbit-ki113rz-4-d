package org.example.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(name= "postit_color", nullable = false)
    private String postItColor;

    @Column (nullable = false)
    private String subject;

    @Column (nullable = false)
    private String message;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column (name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="updated_by")
    private User updatedBy;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    private User author;

    /* ===== Constructors =====*/
    public Post(){
    }

    public Post(String subject, String message){
        this.subject = subject;
        this.message = message;
        this.postItColor = "/images/PostIt_Blue.jpg";
    }

    /* Lifecycle callback ===== */
    @PrePersist
    void prePersist(){
        createdAt = LocalDateTime.now();
        if (postItColor == null || postItColor.isBlank()) {
            postItColor = "/images/PostIt_Blue.jpg";
        }
    }

    /* ===== Getters & setters ====== */
    public int getPostId() {
        return postId;
    }

    public String getPostItColor() {
        return postItColor;
    }

    public void setPostItColor(String postItColor) {
        this.postItColor = postItColor;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public User getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdated(User user) {
        this.updatedBy = user;
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "post_categories",
        joinColumns = @JoinColumn (name = "post_id"),
        inverseJoinColumns = @JoinColumn (name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();


    public List<Category> getCategories(){
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        if (category != null && !categories.contains(category)) {
            categories.add(category);
        }
    }

    /* ===== Debug ===== */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "postId = " + postId + ", " +
            "subject = " + subject + ", " +
            "message = " + message + ", " +
            "createdAt = " + createdAt + ")";
    }
}
