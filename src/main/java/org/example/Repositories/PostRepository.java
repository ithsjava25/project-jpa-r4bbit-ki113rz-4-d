package org.example.Repositories;

import org.example.Entities.Post;

import java.util.List;
import java.util.Optional;

/**
 * Handles posts data from database
 */
public interface PostRepository {
    //Add methods here:
    Post save(Post post);

    Optional<Post> findById(int postId);

    List<Post> findAll();

    void deleteById(int postId);
}
