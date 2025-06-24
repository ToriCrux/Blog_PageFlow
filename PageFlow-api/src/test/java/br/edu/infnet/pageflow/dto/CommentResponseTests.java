package br.edu.infnet.pageflow.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommentResponseTests {

    @Test
    void testConstructorAndGetters() {
        CommentResponse response = new CommentResponse(1, "Great post!", true);

        assertEquals(1, response.getId());
        assertEquals("Great post!", response.getContent());
        assertTrue(response.isApproved());
    }

    @Test
    void testSetters() {
        CommentResponse response = new CommentResponse(0, "", false);

        response.setId(42);
        response.setContent("Updated content");
        response.setApproved(true);

        assertEquals(42, response.getId());
        assertEquals("Updated content", response.getContent());
        assertTrue(response.isApproved());
    }
}
