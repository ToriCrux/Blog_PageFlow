package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    // exemplos de queries que podem ser criadas além das
    // queries default da classe CrudRepository
    @Query("from Category")
    Collection<Category> getAllCategories(Sort sort);
}
