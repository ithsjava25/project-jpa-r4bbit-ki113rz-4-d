package org.example.Services;

import org.example.Entities.Category;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repositories.CategoryRepository;
import org.example.Repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Handles anything related to posts
 * Calls PostRepository if needed
 */
public class PostServiceImpl implements PostService {
    private final PostRepository postRepo;
    private final CategoryRepository categoryRepo;

    private static final List<String> POSTIT_IMAGES = List.of(
        "/Images/PostIt_Yellow.jpg",
        "/Images/PostIt_Blue.jpg",
        "/Images/PostIt_LightGreen.jpg",
        "/Images/PostIt_Pink.jpg",
        "/Images/PostIt_Purple.jpg"
    );

    private String randomPostItColor() {
        return POSTIT_IMAGES.get(new Random().nextInt(POSTIT_IMAGES.size()));
    }

    public PostServiceImpl(PostRepository postRepo, CategoryRepository categoryRepo) {
        this.postRepo = postRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Post createPost(String subject, String message,List<Long> categoryIds, User author) {

        if (subject == null || subject.isBlank())
            throw new IllegalArgumentException("Subject missing");
        if (message == null || message.isBlank())
            throw new IllegalArgumentException("Message missing");
        if (author == null)
            throw new IllegalStateException("No logged in user");
        if (categoryIds == null || categoryIds.isEmpty())
            throw new IllegalArgumentException("At least one category required");


        Post post = new Post(subject, message);

        for (Long id : categoryIds) {
            Category category = categoryRepo.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

            post.addCategory(category);
        }

        post.setAuthor(author);
        post.setPostItColor(randomPostItColor());

        return postRepo.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepo.getPostById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    @Override
    public Post updatePost(Post post, String subject, String message, List<Long> categoryIds, User updatedBy) {
        post.setSubject(subject);
        post.setMessage(message);

        post.setUpdated(updatedBy);

        post.getCategories().clear();
        for (Long id : categoryIds) {
            Category c = categoryRepo.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
            post.getCategories().add(c);
        }
        Post saved = postRepo.save(post);
        return postRepo.findByIdWithAuthorAndCategories(saved.getPostId());
    }

    @Override
    public void deletePost(Post post) {
        if (post == null)
            throw new IllegalArgumentException("Post cannot be null");
        if (post.getPostId() == 0)
            throw new IllegalArgumentException("Post id missing");

        postRepo.deleteById(post.getPostId());
    }

    @Override
    public List<Post> getPostsByUser(User user) {
        return postRepo.getPostsByUser(user); //Temporary
        //TODO: return only users posts
    }

    private void validate(Post post){
        if (post.getSubject() == null || post.getSubject().isBlank())
            throw new IllegalArgumentException("Subject missing");
        if (post.getMessage() == null || post.getMessage().isBlank())
            throw new IllegalArgumentException("Message missing");
    }
}
