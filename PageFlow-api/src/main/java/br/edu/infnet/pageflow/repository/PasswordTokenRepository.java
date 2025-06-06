package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Integer> {

    @Query("SELECT p FROM PasswordResetToken p WHERE p.blogUser.id = :userId")
    PasswordResetToken findTokenByBlogUser(Integer userId);

    PasswordResetToken findByToken(String token);

}
