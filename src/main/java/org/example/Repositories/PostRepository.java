package org.example.Repositories;

import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Handles posts data from database
 */
public interface PostRepository {
    //Add methods here:
    Post save(Post post);
    Optional<Post> getPostById(Long id);
    List<Post> findAll();
    void deleteById(int postId);
    Post updatePost(Post post);

    List<Post> getPostsByUser(User user);
}
