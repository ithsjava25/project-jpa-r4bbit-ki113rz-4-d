package org.example.Services;

import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Handles anything related to posts
 * Calls PostRepository if needed
 */
public interface PostService {
    Post createPost(String subject, String message, Long categoryId, User author);
    List<Post> getAllPosts();
    void updatePost(Post post);
    void deletePost(Post post);

    List<Post> getPostsByUser(User user);
}
