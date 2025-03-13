package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.entities.Post;
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

    @GetMapping
    public ResponseEntity<Collection<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @PostMapping("/new")
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {

        if (postRequest.getAuthorId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

//        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), null);
        Post createdPost = postService.createPost(postRequest);

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

    @PostMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Post> addTag(@PathVariable Integer postId, @PathVariable Integer tagId) {
        return ResponseEntity.ok(postService.addTagToPost(postId, tagId));
    }

    @DeleteMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable Integer postId, @PathVariable Integer tagId) {
        postService.removeTagFromPost(postId, tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{categoryName}")
    public ResponseEntity<Collection<Post>> getPostsByCategory(@PathVariable String categoryName) {
        Collection<Post> posts = postService.findByCategoryName(categoryName);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tagSearch/{tagName}")
    public ResponseEntity<Collection<Post>> getPostsByTag(@PathVariable String tagName) {
        Collection<Post> posts = postService.findByTagName(tagName);
        return ResponseEntity.ok(posts);
    }
}
