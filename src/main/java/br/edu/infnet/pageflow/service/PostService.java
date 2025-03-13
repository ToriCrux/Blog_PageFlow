package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRelationRepository postTagRelationRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, PostTagRelationRepository postTagRelationRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.postTagRelationRepository = postTagRelationRepository;
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

        PostTagRelation postTagRelation = new PostTagRelation();
        postTagRelation.setPost(post);
        postTagRelation.setTag(tag);


        return postTagRelationRepository.save(postTagRelation).getPost();
    }

    public void removeTagFromPost(Integer postId, Integer tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        PostTagRelation postTagRelation = postTagRelationRepository.findByPostAndTag(post, tag)
                .orElseThrow(() -> new RuntimeException("Relation not found"));

        postTagRelationRepository.delete(postTagRelation);
    }

    public Collection<Post> findByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);

        if (category == null) {
            return null;
        }

        Collection<Post> searchedPosts = postRepository.findByCategoryId(category.getId());
        return searchedPosts;

    }

    public Collection<Post> findByTagName(String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        if (tag == null) {
            return null;
        }
        Collection<Post> searchedPosts = postTagRelationRepository.findByTagId(tag.getId());
        return searchedPosts;
    }
}
