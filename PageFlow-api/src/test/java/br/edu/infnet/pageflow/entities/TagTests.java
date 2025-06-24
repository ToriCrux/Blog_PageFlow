package br.edu.infnet.pageflow.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagTests {

    @Test
    public void testTagFields() {
        Tag tag = new Tag();
        tag.setId(100);
        tag.setName("Tech");

        assertEquals(100, tag.getId());
        assertEquals("Tech", tag.getName());
    }
}

