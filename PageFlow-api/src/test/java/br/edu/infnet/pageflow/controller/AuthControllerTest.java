package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.LoginRequest;
import br.edu.infnet.pageflow.dto.NewPasswordRequest;
import br.edu.infnet.pageflow.dto.ResetPasswordRequest;
import br.edu.infnet.pageflow.dto.SignupRequest;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.repository.UserRepository;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.EmailService;
import br.edu.infnet.pageflow.service.JwtTokenService;
import br.edu.infnet.pageflow.service.UserService;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthController authController;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MessageSource messages;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignupUser() throws Exception {
        BlogUser user = new BlogUser("john@example.com", "johndoe");
        user.setId(1);
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setRole(BlogUserRoles.AUTHOR);

        SignupRequest signupRequest = new SignupRequest();

        when(userService.createUser(any(SignupRequest.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.role").value(user.getRole().name()));
    }

    @Test
    void resetPassword() throws Exception {
        BlogUser user = new BlogUser("john@example.com", "johndoe");
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(user.getEmail());

        when(userService.getUserByEmail(resetPasswordRequest.getEmail())).thenReturn(user);
        doNothing().when(emailService).sendEmail(any(), any(), any());
        when(messages.getMessage(eq("message.resetPasswordEmail"), any(), any())).thenReturn("You should receive a password reset email shortly");

        mockMvc.perform(post("/auth/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("You should receive a password reset email shortly"));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest("email@test.com", "password");
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail("email@test.com");

        UserDetails userDetails = new User("email@test.com", "password", Collections.emptyList());

        when(authUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(blogUser));
        when(jwtUtil.generateToken(any())).thenReturn("mockJwt");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("mockJwt")); // ou o que estiver retornando
    }

    @Test
    void testLoginUser_BadCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("email@test.com", "wrongpass");

        // Simula exceção ao tentar autenticar
        doThrow(new BadCredentialsException("Bad creds")).when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginUser_Disabled() throws Exception {
        LoginRequest loginRequest = new LoginRequest("email@test.com", "password");

        // Simula exceção de usuário desabilitado
        doThrow(new DisabledException("Disabled user")).when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden()); // HTTP 403
    }
    @Test
    void testSaveNewPassword_TokenInvalid() throws Exception {
        NewPasswordRequest passwordRequest = new NewPasswordRequest("oldPass", "invalidToken", "newPass");

        when(jwtTokenService.validatePasswordResetToken("invalidToken")).thenReturn("expired");
        when(messages.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Token expirado");

        mockMvc.perform(post("/auth/saveNewPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token expirado"));
    }

    @Test
    void testSaveNewPassword_UserNotFound() throws Exception {
        NewPasswordRequest passwordRequest = new NewPasswordRequest("oldPass", "valid", "newPass");

        when(jwtTokenService.validatePasswordResetToken("valid")).thenReturn("valid");
        when(userService.getUserByPasswordResetToken("valid")).thenReturn(null);
        when(messages.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Usuário não encontrado");

        mockMvc.perform(post("/auth/saveNewPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Usuário não encontrado"));
    }

    @Test
    void testSaveNewPassword_Success() throws Exception {
        NewPasswordRequest passwordRequest = new NewPasswordRequest("oldPass", "valid", "newPass");
        BlogUser user = new BlogUser();

        when(jwtTokenService.validatePasswordResetToken("valid")).thenReturn("valid");
        when(userService.getUserByPasswordResetToken("valid")).thenReturn(user);
        when(messages.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Senha alterada");

        mockMvc.perform(post("/auth/saveNewPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Senha alterada"));
    }

    @Test
    void testValidateAndRespondToResetToken_valid() throws Exception {
        when(jwtTokenService.validatePasswordResetToken("token")).thenReturn("valid");
        when(messages.getMessage(eq("token.message.valid"), any(), any(Locale.class)))
                .thenReturn("Token válido");

        mockMvc.perform(post("/auth/changePassword/token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Token válido"));
    }

    @Test
    void testValidateAndRespondToResetToken_expired() throws Exception {
        when(jwtTokenService.validatePasswordResetToken("token")).thenReturn("expired");
        when(messages.getMessage(eq("token.message.expired"), any(), any(Locale.class)))
                .thenReturn("Token expirado");

        mockMvc.perform(post("/auth/changePassword/token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token expirado"));
    }
}