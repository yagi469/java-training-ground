package com.example.week6.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.week6.blog.repository.PostRepository;
import com.example.week6.blog.repository.CategoryRepository;
import com.example.week6.blog.repository.UserRepository;
import com.example.week6.blog.dto.PostRequest;
import com.example.week6.blog.dto.PostResponse;
import com.example.week6.blog.entity.Post;
import com.example.week6.blog.entity.Category;
import com.example.week6.blog.entity.User;
import com.example.week6.blog.exception.CategoryNotFoundException;
import com.example.week6.blog.exception.PostNotFoundException;
import com.example.week6.blog.exception.UserNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PostResponse getPostById(Long id) {
        return toResponse(postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id)));
    }

    @Transactional
    public PostResponse createPost(PostRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(
                        () -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Post post = toEntity(request, category, user);
        Post savedPost = postRepository.save(post);
        return toResponse(savedPost);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        if (!existingPost.getCategory().getId().equals(request.getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Category not found with id: " + request.getCategoryId()));
            existingPost.setCategory(category);
        }
        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        Post updatedPost = postRepository.save(existingPost);
        return toResponse(updatedPost);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getCategory().getId());
    }

    private Post toEntity(PostRequest request, Category category, User user) {
        return new Post(
                null,
                request.getTitle(),
                request.getContent(),
                user,
                category,
                null, // createdAt
                null // updatedAt
        );
    }
}
