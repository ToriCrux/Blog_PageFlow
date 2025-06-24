package br.edu.infnet.pageflow.security;

import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTests {

    private JwtUtil jwtUtil;
    private BlogUser blogUser;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        blogUser = new BlogUser();
        blogUser.setId(1);
        blogUser.setEmail("test@example.com");
        blogUser.setUsername("testuser");
        blogUser.setRole(BlogUserRoles.AUTHOR);

        mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("test@example.com");
    }

    @Test
    void testValidateToken_ShouldReturnTrue_ForValidToken() {
        String token = jwtUtil.generateToken(Optional.of(blogUser));
        assertTrue(jwtUtil.validateToken(token, mockUserDetails));
    }

    @Test
    void testValidateToken_ShouldReturnFalse_WhenUsernameDoesNotMatch() {
        String token = jwtUtil.generateToken(Optional.of(blogUser));
        when(mockUserDetails.getUsername()).thenReturn("other@example.com");
        assertFalse(jwtUtil.validateToken(token, mockUserDetails));
    }

    @Test
    void testIsTokenExpired_ShouldReturnFalse_ForNewToken() {
        String token = jwtUtil.generateToken(Optional.of(blogUser));
        assertFalse(invokeIsTokenExpired(token));
    }

    // Helper para acessar método privado usando reflexão
    private boolean invokeIsTokenExpired(String token) {
        try {
            var method = JwtUtil.class.getDeclaredMethod("isTokenExpired", String.class);
            method.setAccessible(true);
            return (boolean) method.invoke(jwtUtil, token);
        } catch (Exception e) {
            fail("Falha ao invocar isTokenExpired: " + e.getMessage());
            return true;
        }
    }
}
