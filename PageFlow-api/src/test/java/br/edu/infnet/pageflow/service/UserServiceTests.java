package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.SignupRequest;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.PasswordTokenRepository;
import br.edu.infnet.pageflow.repository.UserRepository;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock private UserRepository userRepository;
    @Mock private PasswordTokenRepository passwordTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private SignupRequest signupRequest;
    private BlogUser user;

    @BeforeEach
    void setup() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setName("Test User");
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("password");
        signupRequest.setRole(BlogUserRoles.AUTHOR);

        user = new Author();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("encoded");
    }

    @Test
    void testGetUsers() {
        when(userRepository.getAllUsers()).thenReturn(List.of(user));
        Collection<BlogUser> users = userService.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Optional<BlogUser> found = userService.getUserById(1);
        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        BlogUser found = userService.getUserByEmail("test@example.com");
        assertEquals(user, found);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("none@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail("none@example.com"));
    }

    @Test
    void testCreateUser_NewEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BlogUser created = userService.createUser(signupRequest);

        assertNotNull(created);
        assertEquals("encodedPassword", created.getPassword());
        assertEquals(signupRequest.getEmail(), created.getEmail());
        assertEquals(signupRequest.getUsername(), created.getUsername());
    }

    @Test
    void testCreateUser_EmailExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        BlogUser result = userService.createUser(signupRequest);
        assertNull(result);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1)).thenReturn(true);
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userService.deleteUser(1));
    }

    @Test
    void testInstantiateUserByRole_Author() {
        SignupRequest request = new SignupRequest();
        request.setRole(BlogUserRoles.AUTHOR);
        BlogUser created = new Author();
        assertInstanceOf(Author.class, created);
    }

    @Test
    void testInstantiateUserByRole_Administrator() {
        SignupRequest request = new SignupRequest();
        request.setRole(BlogUserRoles.ADMINISTRATOR);
        BlogUser created = new BlogAdministrator();
        assertInstanceOf(BlogAdministrator.class, created);
    }

    @Test
    void testInstantiateUserByRole_Visitor() {
        SignupRequest request = new SignupRequest();
        request.setRole(BlogUserRoles.VISITOR);
        BlogUser created = new Visitor();
        assertInstanceOf(Visitor.class, created);
    }

    @Test
    void testCreatePasswordResetTokenForUser() {
        PasswordResetToken token = new PasswordResetToken("abc123", user);
        when(passwordTokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        userService.createPasswordResetTokenForUser(user, "abc123");
        verify(passwordTokenRepository).save(any());
    }

    @Test
    void testGetUserByPasswordResetToken() {
        PasswordResetToken token = new PasswordResetToken("abc123", user);
        when(passwordTokenRepository.findByToken("abc123")).thenReturn(token);

        BlogUser result = userService.getUserByPasswordResetToken("abc123");
        assertEquals(user, result);
    }

    @Test
    void testChangeUserPassword() {
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");
        userService.changeUserPassword(user, "newpass");
        verify(userRepository).save(user);
        assertEquals("encodedPass", user.getPassword());
    }
}
