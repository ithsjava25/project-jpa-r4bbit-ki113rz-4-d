package org.example;

import java.util.List;

public interface PostService {
    void createPost(Post post, User user);
    List<Post> getAllPosts();
    void updatePost(Post post);
    void deletePost(Post post);
}
