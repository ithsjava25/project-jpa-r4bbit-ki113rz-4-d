package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    public List<Post> getPosts(){
        return posts;
    }

    public void addPost(Post post){
        if (post != null && !posts.contains(post)) {
            posts.add(post);
        }
    }

    public Category() {}

    public Category(String name){
        this.name = name;
    }

    public Long getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Category{" +
            "categoryId=" + categoryId +
            ", name='" + name + '\'' +
            '}';
    }
}
