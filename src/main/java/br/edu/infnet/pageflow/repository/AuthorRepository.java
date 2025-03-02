package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
}
