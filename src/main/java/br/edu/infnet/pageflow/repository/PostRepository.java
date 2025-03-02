package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    // exemplos de queries que podem ser criadas além das
    // queries default da classe CrudRepository
    @Query("SELECT p FROM Post p")
    Collection<Post> getAllPosts();
}
