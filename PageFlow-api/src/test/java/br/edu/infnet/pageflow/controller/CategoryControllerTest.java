package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.CategoryRequest;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        Category category = new Category();
        category.setId(1);
        category.setName("test");

        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryService.getCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/categories")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("test"));
    }

    @Test
    void testCreateCategory() throws Exception {
        Category category = new Category();
        category.setId(1);
        category.setName("test");

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName(category.getName());

        when(categoryService.createCategory(categoryRequest.getName())).thenReturn(category);

        mockMvc.perform(post("/api/v1/categories/new")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"));

    }

    @Test
    void testUpdateCategory() throws Exception {

        Category category = new Category();
        category.setId(1);
        category.setName("test");

        Category updatedCategory = new Category();
        updatedCategory.setId(1);
        updatedCategory.setName("updated test");

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName(updatedCategory.getName());

        when(categoryService.updateCategory(category.getId(), "updated test")).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/v1/categories/{id}", category.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("updated test"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Category category = new Category();
        category.setId(1);
        category.setName("Category to delete");

        doNothing().when(categoryService).deleteCategory(category.getId());

        mockMvc.perform(delete("/api/v1/categories/{id}", category.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}