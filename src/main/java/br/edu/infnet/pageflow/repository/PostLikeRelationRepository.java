package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.entities.PostLikeRelation;
import br.edu.infnet.pageflow.entities.ids.PostLikeRelationId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PostLikeRelationRepository extends CrudRepository<PostLikeRelation, PostLikeRelationId> {
    @Query("SELECT plr.user.id FROM PostLikeRelation plr WHERE plr.post.id = :postId")
    List<Integer> findUserIdsByPostId(@Param("postId") Integer postId);

    @Query("SELECT COUNT(*) AS result FROM PostLikeRelation plr WHERE plr.post.id = :postId")
    Integer countByCommentId(@Param("postId") Integer postId);
}
