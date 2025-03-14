package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<Collection<Comment>> getAllComments() {
        Collection<Comment> comments = commentService.getComments();
        System.out.println("comments: " + comments);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/new")
    public ResponseEntity<Comment> createComment(@RequestBody @Valid Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(comment));
    }
}
