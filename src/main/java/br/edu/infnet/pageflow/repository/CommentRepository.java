package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    // exemplos de queries que podem ser criadas além das
    // queries default da classe CrudRepository
    @Query("from Comment")
    Collection<Comment> getAllComments(Sort sort);
}
