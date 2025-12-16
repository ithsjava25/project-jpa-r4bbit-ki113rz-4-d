package org.example;

import java.util.List;

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
    public void createPost(Post post, User user) {
        validate(post);
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        user.addPost(post);

        userRepo.save(user);
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
