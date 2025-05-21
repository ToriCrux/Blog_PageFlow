package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.PostResponse;
import br.edu.infnet.pageflow.dto.UserInfoResponse;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.repository.PostLikeRelationRepository;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.PostService;
import br.edu.infnet.pageflow.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostCommentRelationRepository postCommentRelationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostLikeRelationRepository postLikeRelationRepository;

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

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> addLike(@PathVariable Integer postId, Principal principal) {
        String email = principal.getName();
        BlogUser user = userService.getUserByEmail(email);
        try {
            postService.addLikePost(postId, user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<?> removeLike(@PathVariable Integer postId, Principal principal) {
        String email = principal.getName();
        BlogUser user = userService.getUserByEmail(email);
        try {
            postService.removeLikePost(postId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("{postId}/likes")
    public ResponseEntity<List<UserInfoResponse>> getUsersWhoLikedPost(@PathVariable Integer postId) {
        try {
            List<UserInfoResponse> users = postService.getUsersWhoLikedPost(postId);
            return ResponseEntity.ok(users);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("{postId}/likes/count")
    public ResponseEntity<Integer> countUsersWhoLikedPost(@PathVariable Integer postId) {
        try {
            Integer count = postService.countUsersWhoLikedPost(postId);
            return ResponseEntity.ok(count);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
