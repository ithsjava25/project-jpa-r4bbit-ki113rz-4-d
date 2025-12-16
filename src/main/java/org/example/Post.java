package org.example;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany (mappedBy = "posts", fetch = FetchType.LAZY)
    private List<User> authors = new ArrayList<>();
}
