package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Author;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.repository.PostRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(Post post, Integer authorId) {

        BlogUser author = userRepository.findAuthorById(authorId);
        post.setAuthor((Author) author);

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
}
