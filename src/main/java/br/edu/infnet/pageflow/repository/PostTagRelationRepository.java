package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.entities.PostTagRelation;
import br.edu.infnet.pageflow.entities.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostTagRelationRepository extends CrudRepository<PostTagRelation, Integer> {

    Optional<PostTagRelation> findByPostAndTag(Post post, Tag tag);
}
