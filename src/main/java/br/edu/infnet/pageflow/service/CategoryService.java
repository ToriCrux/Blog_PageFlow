package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.model.Category;
import br.edu.infnet.pageflow.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Collection<Category> getCategories() {
        return categoryRepository.getAllCategories(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
