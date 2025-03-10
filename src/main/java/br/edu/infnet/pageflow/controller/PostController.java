package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.model.Post;
import br.edu.infnet.pageflow.model.PostRequest;
import br.edu.infnet.pageflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping(value = "/")
    public ResponseEntity<Collection<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @PostMapping("/new")
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        if (postRequest.getAuthorId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), null);
        Post createdPost = postService.createPost(post, postRequest.getAuthorId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer id, @RequestBody Post updatedPost) {
        return ResponseEntity.ok(postService.updatePost(id, updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
