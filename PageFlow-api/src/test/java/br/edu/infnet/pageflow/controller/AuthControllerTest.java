package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.LoginRequest;
import br.edu.infnet.pageflow.dto.LoginResponse;
import br.edu.infnet.pageflow.dto.ResetPasswordRequest;
import br.edu.infnet.pageflow.dto.SignupRequest;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.EmailService;
import br.edu.infnet.pageflow.service.JwtTokenService;
import br.edu.infnet.pageflow.service.UserService;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

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

//    @Test
//    void testLoginUser() throws Exception {
//        BlogUser user = new BlogUser("john@example.com", "johndoe");
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(user.getEmail());
//        loginRequest.setPassword(user.getPassword());
//
//        UserDetails userDetails = mock(UserDetails.class);
//
//        LoginResponse loginResponse = new LoginResponse(jwtUtil.generateToken(userDetails.getUsername()));
//
//
//        when(userDetails.getUsername()).thenReturn(user.getEmail());
//        when(authUserDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
//        when(jwtUtil.generateToken(user.getEmail())).thenReturn(String.valueOf(loginResponse));
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.jwt").value(String.valueOf(loginResponse)));
//    }

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
    void changePassword() {
    }

    @Test
    void saveNewPassword() {
    }
}