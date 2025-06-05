package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("from Category")
    Collection<Category> getAllCategories(Sort sort);

    Category findByName(String name);
}
