package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query("from Post")
    Collection<Post> getAllPosts(Sort by);
}
