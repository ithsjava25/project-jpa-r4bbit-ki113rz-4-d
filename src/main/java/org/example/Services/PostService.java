package org.example;

import java.util.List;

/**
 * Handles anything related to posts
 * Calls PostRepository if needed
 */
public interface PostService {
    void createPost(Post post, User user);
    List<Post> getAllPosts();
    void updatePost(Post post);
    void deletePost(Post post);
}
