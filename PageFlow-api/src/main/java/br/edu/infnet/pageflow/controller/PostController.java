package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.PostResponse;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostCommentRelationRepository postCommentRelationRepository;

    @GetMapping
    public ResponseEntity<Collection<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @PostMapping("/new")
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {

        if (postRequest.getAuthorId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

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

    @PostMapping("/{postId}/comment")
    public ResponseEntity<PostResponse> addComment(@PathVariable Integer postId, @RequestBody CommentRequest commentRequest) {

        Post existingPost = postService.findById(postId);

        if (existingPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Comment comment = commentService.createComment(commentRequest);

        return ResponseEntity.ok(postService.addCommentToPost(existingPost.getId(), comment.getId()));
    }

    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<Post> updateComment(@PathVariable Integer postId, @PathVariable Integer commentId, @RequestBody CommentRequest commentRequest) {
        Post existingPost = postService.findById(postId);
        if (existingPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Comment existingComment = postService.getCommentById(commentId);
        if (existingComment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        existingComment.setContent(commentRequest.getContent());
        existingComment.setApproved(commentRequest.isApproved());
        existingComment.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(existingPost);
    }

    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId) {
        Post existingPost = postService.findById(postId);
        if (existingPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Comment existingComment = postService.getCommentById(commentId);
        if (existingComment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        postService.deleteCommentFromPost(existingPost.getId(), commentId);

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
