package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.CategoryRequest;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Collection<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping("/new")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequest.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) {
        Category updatedCategory = categoryService.updateCategory(id, categoryRequest.getName());
        return updatedCategory != null
                ? ResponseEntity.ok(updatedCategory)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
