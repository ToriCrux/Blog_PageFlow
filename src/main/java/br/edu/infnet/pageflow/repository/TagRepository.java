package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    @Query("from Tag")
    Collection<Tag> getAllTags(Sort sort);

    @Query("SELECT t FROM Tag t WHERE t.name = :name")
    Tag findByName(@Param("name") String name);
}
