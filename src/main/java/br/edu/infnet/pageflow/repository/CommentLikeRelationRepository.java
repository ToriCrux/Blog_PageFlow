package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.CommentLikeRelation;
import br.edu.infnet.pageflow.entities.ids.CommentLikeRelationId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentLikeRelationRepository  extends CrudRepository<CommentLikeRelation, CommentLikeRelationId> {
    @Query("SELECT clr.user.id FROM CommentLikeRelation clr WHERE clr.comment.id = :commentId")
    List<Integer> findUserIdsByCommentId(@Param("commentId") Integer commentId);

    @Query("SELECT COUNT(*) AS result FROM CommentLikeRelation clr WHERE clr.comment.id = :commentId")
    Integer countByCommentId(@Param("commentId") Integer commentId);
}
