package org.example.Services;

import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repositories.PostRepository;
import org.example.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles anything related to posts
 * Calls PostRepository if needed
 */
public class PostServiceImpl implements PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;


    public PostServiceImpl(PostRepository postRepo, UserRepository userRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Post createPost(Post post, User user) {
        if (post == null || user == null) {
            throw new IllegalArgumentException("Post and user cannot be null");
        }
        return postRepo.createPost(post, user);
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return postRepo.getPostById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    @Override
    public void updatePost(Post post) {
        if (post == null || post.getPostId() == 0){
            throw new IllegalArgumentException("Post or postId missing");
        }
        validate(post);

        postRepo.save(post);
    }

    @Override
    public void deletePost(Post post) {
        if (post == null)
            throw new IllegalArgumentException("Post cannot be null");
        if (post.getPostId() == 0)
            throw new IllegalArgumentException("Post id missing");

        postRepo.deleteById(post.getPostId());
    }

    private void validate(Post post){
        if (post.getSubject() == null || post.getSubject().isBlank())
            throw new IllegalArgumentException("Subject missing");
        if (post.getMessage() == null || post.getMessage().isBlank())
            throw new IllegalArgumentException("Message missing");
    }
}
