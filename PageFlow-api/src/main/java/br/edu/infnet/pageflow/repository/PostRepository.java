package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Collection<Post> findByCategoryId(Integer id);

    Optional<Post> getPostById(Integer postId);

    List<Post> findAllByOrderByCreatedAtDesc();

}
