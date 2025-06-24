package br.edu.infnet.pageflow.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorTests {

    @Test
    public void testAuthorFields() {
        Author author = new Author();
        author.setId(1);
        author.setUsername("authorUser");
        author.setEmail("author@example.com");
        author.setPassword("secret");

        assertEquals(1, author.getId());
        assertEquals("authorUser", author.getUsername());
        assertEquals("author@example.com", author.getEmail());
        assertEquals("secret", author.getPassword());
    }
}

