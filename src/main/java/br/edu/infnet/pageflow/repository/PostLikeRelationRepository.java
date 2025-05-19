package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.PostLikeRelation;
import br.edu.infnet.pageflow.entities.ids.PostLikeRelationId;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeRelationRepository extends CrudRepository<PostLikeRelation, PostLikeRelationId> {

}
