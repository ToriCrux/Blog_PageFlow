package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.CategoryRepository;
import br.edu.infnet.pageflow.repository.PostRepository;
import br.edu.infnet.pageflow.repository.TagRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Post createPost(PostRequest postRequest) {

        BlogUser author = userRepository.findAuthorById(postRequest.getAuthorId());
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor((Author) author);

        Optional<Category> category = categoryRepository.findById(postRequest.getCategoryId());
        category.ifPresent(post::setCategory);

        return postRepository.save(post);
    }

    public Collection<Post> getPosts() {
        return (Collection<Post>) postRepository.findAll();
    }

    public Post updatePost(Integer id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.deleteById(id);
    }

    public Post addTagToPost(Integer postId, Integer tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));

        post.getTags().add(tag);
        return postRepository.save(post);
    }

    public Post removeTagFromPost(Integer postId, Integer tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        post.getTags().remove(tag);
        return postRepository.save(post);
    }
}
