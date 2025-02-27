package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Comment;
import br.edu.infnet.pageflow.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Collection<Comment> getComments(){
        return commentRepository.getAllComments(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Comment createComment(Comment comment){
        return commentRepository.save(comment);
    }
}
