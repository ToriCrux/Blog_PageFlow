package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.CommentLikeRelation;
import br.edu.infnet.pageflow.entities.ids.CommentLikeRelationId;
import org.springframework.data.repository.CrudRepository;

public interface CommentLikeRelationRepository  extends CrudRepository<CommentLikeRelation, CommentLikeRelationId> {

}
