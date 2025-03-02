package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Author;
import br.edu.infnet.pageflow.model.Post;
import br.edu.infnet.pageflow.repository.AuthorRepository;
import br.edu.infnet.pageflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthorRepository authorRepository; // Agora usamos um repositório específico para Author

    public Post createPost(Post post, Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        post.setAuthor(author);
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
