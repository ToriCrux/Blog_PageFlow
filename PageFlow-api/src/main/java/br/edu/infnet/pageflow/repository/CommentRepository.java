package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Query("from Comment")
    Collection<Comment> getAllComments();
}
