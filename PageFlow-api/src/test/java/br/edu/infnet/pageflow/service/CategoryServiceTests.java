package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTests {

    @Mock private CategoryRepository categoryRepository;

    @InjectMocks private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1);
        category.setName("Tech");
    }

    @Test
    void testGetCategories() {
        when(categoryRepository.getAllCategories(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(List.of(category));

        Collection<Category> result = categoryService.getCategories();

        assertEquals(1, result.size());
        assertEquals("Tech", result.iterator().next().getName());
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Category created = categoryService.createCategory("Educação");

        assertEquals("Educação", created.getName());
    }

    @Test
    void testDeleteCategory_Exists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        categoryService.deleteCategory(1);
        verify(categoryRepository).deleteById(1);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(1)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(1));
    }

    @Test
    void testUpdateCategory_Exists() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Category updated = categoryService.updateCategory(1, "Atualizado");

        assertEquals("Atualizado", updated.getName());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        Category updated = categoryService.updateCategory(99, "Nada");

        assertNull(updated);
    }
}
