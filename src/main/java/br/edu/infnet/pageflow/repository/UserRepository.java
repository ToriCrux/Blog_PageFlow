package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.BlogUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<BlogUser, Integer> {

    Optional<BlogUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM BlogUser u")
    Collection<BlogUser> getAllUsers();

}
