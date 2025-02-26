package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.model.Comment;
import br.edu.infnet.pageflow.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/v1/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public ResponseEntity<Collection<Comment>> commentsList() {
        Collection<Comment> comments = commentService.getComments();
        return ResponseEntity.ok(comments);
    }
}
