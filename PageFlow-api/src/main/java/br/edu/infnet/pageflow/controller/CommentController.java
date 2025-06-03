package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.CategoryRequest;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

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
}
