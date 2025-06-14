package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Collection<Category> getCategories() {
        return categoryRepository.getAllCategories(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada!");
        }
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Integer id, String categoryName) {

        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category updatedCategory = existingCategory.get();
            updatedCategory.setName(categoryName);
            return categoryRepository.save(updatedCategory);
        }

        return null;
    }

}
