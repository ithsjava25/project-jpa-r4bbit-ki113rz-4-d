package org.example.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column (nullable = false)
    private String subject;

    @Column (nullable = false)
    private String message;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany (mappedBy = "posts", fetch = FetchType.LAZY)
    private Set<User> authors = new HashSet<>();

    /* ===== Constructors =====*/
    public Post(){
    }

    public Post(String subject, String message){
        this.subject = subject;
        this.message = message;
    }

    /* Lifecycle callback ===== */
    @PrePersist
    void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    /* ===== Getters & setters ====== */
    public int getPostId() {
        return postId;
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

    public Set<User> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<User> authors) {
        this.authors = authors;
    }

    public void addAuthor(User user){
        if(!authors.contains(user)){
            authors.add(user);
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
