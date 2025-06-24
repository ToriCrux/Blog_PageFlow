package br.edu.infnet.pageflow.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorTests {

    @Test
    public void testVisitorFields() {
        Visitor visitor = new Visitor();
        visitor.setId(3);
        visitor.setUsername("visitorUser");
        visitor.setEmail("visitor@example.com");
        visitor.setPassword("visitorpass");
        visitor.setCanComment(true);

        assertEquals(3, visitor.getId());
        assertEquals("visitorUser", visitor.getUsername());
        assertEquals("visitor@example.com", visitor.getEmail());
        assertEquals("visitorpass", visitor.getPassword());
        assertTrue(visitor.getCanComment());
    }
}

