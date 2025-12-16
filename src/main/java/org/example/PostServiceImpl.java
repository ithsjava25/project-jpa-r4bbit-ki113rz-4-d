package org.example;

import java.util.List;

/**
 * Handles anything related to posts
 * Calls PostRepository if needed
 */
public class PostServiceImpl implements PostService {
    private final PostRepository postRepo;
    public PostServiceImpl(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    @Override
    public void createPost(Post post, User user) {

    }

    @Override
    public List<Post> getAllPosts() {
        return List.of();
    }

    @Override
    public void updatePost(Post post) {

    }

    @Override
    public void deletePost(Post post) {

    }
}
