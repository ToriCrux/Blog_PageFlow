package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.entities.Author;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.utils.PostStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostResponseTests {

    @Test
    void testNoArgsConstructor() {
        PostResponse response = new PostResponse();
        assertNull(response.getId());
        assertNull(response.getTitle());
        assertNull(response.getContent());
        assertNull(response.getAuthor());
        assertNull(response.getCategory());
        assertNull(response.getComments());
        assertNull(response.getStatus());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        Author author = new Author();
        Category category = new Category();
        CommentResponse comment = new CommentResponse(1, "comentário", true);
        List<CommentResponse> comments = List.of(comment);

        PostResponse response = new PostResponse(1, "Título", "Conteúdo", author, category, comments, PostStatus.PUBLISHED);

        assertEquals(1, response.getId());
        assertEquals("Título", response.getTitle());
        assertEquals("Conteúdo", response.getContent());
        assertEquals(author, response.getAuthor());
        assertEquals(category, response.getCategory());
        assertEquals(comments, response.getComments());
        assertEquals(PostStatus.PUBLISHED, response.getStatus());
    }

    @Test
    void testSetters() {
        PostResponse response = new PostResponse();

        Author author = new Author();
        Category category = new Category();
        List<CommentResponse> comments = List.of(new CommentResponse(2, "comentário 2", false));

        response.setId(42);
        response.setTitle("Novo título");
        response.setContent("Novo conteúdo");
        response.setAuthor(author);
        response.setCategory(category);
        response.setComments(comments);
        response.setStatus(PostStatus.DRAFT);

        assertEquals(42, response.getId());
        assertEquals("Novo título", response.getTitle());
        assertEquals("Novo conteúdo", response.getContent());
        assertEquals(author, response.getAuthor());
        assertEquals(category, response.getCategory());
        assertEquals(comments, response.getComments());
        assertEquals(PostStatus.DRAFT, response.getStatus());
    }
}
