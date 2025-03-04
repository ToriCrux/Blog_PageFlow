package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.model.Category;
import br.edu.infnet.pageflow.model.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface TagRepository extends CrudRepository<Tag, UUID> {
    // exemplos de queries que podem ser criadas além das
    // queries default da classe CrudRepository
    @Query("from Tag")
    Collection<Tag> getAllTags(Sort sort);
}
