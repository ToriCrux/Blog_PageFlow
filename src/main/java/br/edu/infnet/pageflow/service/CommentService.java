package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Collection<Comment> getComments(){
        System.out.println("** comments repository **");
        System.out.println("comments repository: " + commentRepository.getAllComments());
        return (Collection<Comment>) commentRepository.findAll();
//        return commentRepository.getAllComments();
    }

    public Comment createComment(Comment comment){
        return commentRepository.save(comment);
    }
}
