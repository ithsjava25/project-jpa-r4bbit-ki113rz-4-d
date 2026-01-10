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
    Post createPost(String subject, String message, List<Long> categoryIds, User author);
    List<Post> getAllPosts();
    Post updatePost(Post post, String subject, String message, List<Long> categoryIds, User updatedBy);
    void deletePost(Post post);
    List<Post> getPostsByUser(User user);
}
