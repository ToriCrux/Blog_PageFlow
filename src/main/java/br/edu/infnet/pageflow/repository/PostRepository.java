package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.utils.PostStatus;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.status = br.edu.infnet.pageflow.utils.PostStatus.PUBLISHED AND p.category.id = :categoryId")
    Collection<Post> findByCategoryId(@Param("categoryId") Integer categoryId);

    Optional<Post> getPostById(Integer postId);

//    @Query("SELECT p FROM Post p WHERE p.status = br.edu.infnet.pageflow.utils.PostStatus.PUBLISHED ORDER BY p.createdAt DESC")
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.status = br.edu.infnet.pageflow.utils.PostStatus.DRAFT AND p.author.id = :authorId")
    Optional<Post> getDraftPostByAuthor(@Param("authorId") Integer authorId);


}
