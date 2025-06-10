package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

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

    @Test
    void testGetAllUsers() throws Exception {
        List<BlogUser> users = List.of(new BlogUser("john@example.com", "johndoe"));

        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        BlogUser user = new BlogUser("john@example.com", "johndoe");
        user.setId(1);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v1/users/{id}", user.getId())
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"));

    }

    @Test
    void deleteUser() throws Exception {
        BlogUser user = new BlogUser("john@example.com", "johndoe");
        user.setId(1);

        doNothing().when(userService).deleteUser(user.getId());

        mockMvc.perform(delete("/api/v1/users/{id}", user.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}