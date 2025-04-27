package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostCommentRelationRepository extends JpaRepository<PostCommentRelation, Long> {

    List<PostCommentRelation> findAllByPostId(Integer postId);

    Optional<PostCommentRelation> findByPostIdAndCommentId(Integer postId, Integer commentId);

    List<PostCommentRelation> findAllByCommentId(Long commentId);

    @Query("SELECT c FROM Comment c JOIN PostCommentRelation pcr ON c.id = pcr.comment.id WHERE pcr.comment.id = :commentId")
    Comment findCommentById(@Param("commentId") Integer commentId);
}
