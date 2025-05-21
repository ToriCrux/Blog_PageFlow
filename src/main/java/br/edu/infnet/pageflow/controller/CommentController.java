package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.CategoryRequest;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.UserInfoResponse;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private UserService userService;

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<Collection<Comment>> getAllComments() {
        Collection<Comment> comments = commentService.getComments();
        System.out.println("comments: " + comments);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> addLike(@PathVariable Integer commentId, Principal principal) {
        String email = principal.getName();
        BlogUser user = userService.getUserByEmail(email);
        try {
            commentService.addLikeComment(commentId, user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<?> removeLike(@PathVariable Integer commentId, Principal principal) {
        String email = principal.getName();
        BlogUser user = userService.getUserByEmail(email);
        try {
            commentService.removeLikeComment(commentId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("{commentId}/likes")
    public ResponseEntity<List<UserInfoResponse>> getUsersWhoLikedComment(@PathVariable Integer commentId) {
        try {
            List<UserInfoResponse> users = commentService.getUsersWhoLikedComment(commentId);
            return ResponseEntity.ok(users);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("{commentId}/likes/count")
    public ResponseEntity<Integer> countUsersWhoLikedComment(@PathVariable Integer commentId) {
        try {
            Integer count = commentService.countUsersWhoLikedComment(commentId);
            return ResponseEntity.ok(count);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
