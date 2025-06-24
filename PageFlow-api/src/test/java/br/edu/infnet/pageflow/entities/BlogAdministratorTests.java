package br.edu.infnet.pageflow.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlogAdministratorTests {

    @Test
    public void testAdminFields() {
        BlogAdministrator admin = new BlogAdministrator();
        admin.setId(2);
        admin.setUsername("adminUser");
        admin.setEmail("admin@example.com");
        admin.setPassword("adminpass");

        assertEquals(2, admin.getId());
        assertEquals("adminUser", admin.getUsername());
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("adminpass", admin.getPassword());
    }
}

