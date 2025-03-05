package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    // exemplos de queries que podem ser criadas além das
    // queries default da classe CrudRepository
    @Query("from Post")
    Collection<Post> getAllPosts(Sort by);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE LOWER(p.category.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Collection<Post> findByCategoryOrTagName(@Param("name") String name);
}
