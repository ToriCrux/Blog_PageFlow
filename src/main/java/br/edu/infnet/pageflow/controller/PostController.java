package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.model.Post;
import br.edu.infnet.pageflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

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
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post));
    }

    @PutMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Post> addTag(@PathVariable Integer postId, @PathVariable UUID tagId) {
        return ResponseEntity.ok(postService.addTagToPost(postId, tagId));
    }

    @DeleteMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Post> removeTag(@PathVariable Integer postId, @PathVariable UUID tagId) {
        return ResponseEntity.ok(postService.removeTagFromPost(postId, tagId));
    }

    @GetMapping("/buscar/{name}")
    public ResponseEntity<Collection<Post>> getPostsByCategoryOrTagName(@PathVariable String name) {
        Collection<Post> posts = postService.findByCategoryOrTagName(name);
        return ResponseEntity.ok(posts);
    }
}
