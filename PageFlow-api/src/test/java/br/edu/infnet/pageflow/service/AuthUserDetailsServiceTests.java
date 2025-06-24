package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.repository.UserRepository;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthUserDetailsServiceTests {

    @Mock private UserRepository userRepository;

    @InjectMocks private AuthUserDetailsService authUserDetailsService;

    private BlogUser user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new BlogUser();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void testLoadUserByUsername_Admin() {
        user.setRole(BlogUserRoles.ADMINISTRATOR);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = authUserDetailsService.loadUserByUsername("user@example.com");

        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRATOR")));
    }

    @Test
    void testLoadUserByUsername_Author() {
        user.setRole(BlogUserRoles.AUTHOR);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = authUserDetailsService.loadUserByUsername("user@example.com");

        assertEquals("ROLE_AUTHOR", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_Visitor() {
        user.setRole(BlogUserRoles.VISITOR);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = authUserDetailsService.loadUserByUsername("user@example.com");

        assertEquals("ROLE_VISITOR", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                authUserDetailsService.loadUserByUsername("notfound@example.com"));
    }
}
