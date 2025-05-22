package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.UserInfoResponse;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserService userService;

    @Test
    void testGetAllComments() throws Exception {
        Comment comment = new Comment();
        comment.setContent("This is a comment");
        comment.setApproved(true);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentService.getComments()).thenReturn(comments);

        mockMvc.perform(get("/api/v1/comments")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].content").value("This is a comment"));
    }

    @Test
    void testCreateComment() throws Exception {

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("This is a comment");
        commentRequest.setApproved(true);

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setApproved(commentRequest.isApproved());

        when(commentService.createComment(commentRequest)).thenReturn(comment);

        mockMvc.perform(get("/api/v1/comments")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testAddLikeCommentSuccessfully() throws Exception {
        Integer commentId = 100;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        when(commentService.addLikeComment(commentId, userMock)).thenReturn(true);

        mockMvc.perform(post("/api/v1/comments/{commentId}/like", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testUserCannotLikeCommentTwice() throws Exception {
        Integer commentId = 100;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        when(commentService.addLikeComment(commentId, userMock)).thenReturn(true);

        mockMvc.perform(post("/api/v1/comments/{commentId}/like", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        when(commentService.addLikeComment(commentId, userMock)).thenReturn(false);

        mockMvc.perform(post("/api/v1/comments/{commentId}/like", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddLikeCommentWhenCommentIsNotFound() throws Exception {
        Integer commentIdAbsent = 999;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new EntityNotFoundException()).when(commentService).addLikeComment(commentIdAbsent, userMock);

        mockMvc.perform(post("/api/v1/comments/{commentId}/like", commentIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddLikeCommentWhenInternalServerErrorOccurs() throws Exception {
        Integer commentId = 100;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new RuntimeException("Erro inesperado")).when(commentService).addLikeComment(commentId, userMock);

        mockMvc.perform(post("/api/v1/comments/{commentId}/like", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRemoveLikeCommentSuccessfully() throws Exception {
        Integer commentId = 200;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doNothing().when(commentService).removeLikeComment(commentId, userMock.getId());

        mockMvc.perform(delete("/api/v1/comments/{commentId}/like", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveLikeCommentWhenCommentIsNotFound() throws Exception {
        Integer commentIdAbsent = 888;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new EntityNotFoundException()).when(commentService).removeLikeComment(commentIdAbsent, userMock.getId());

        mockMvc.perform(delete("/api/v1/comments/{commentId}/like", commentIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveLikeCommentWhenInternalServerErrorOccurs() throws Exception {
        Integer commentId = 200;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new RuntimeException("Unexpected error")).when(commentService).removeLikeComment(commentId, userMock.getId());

        mockMvc.perform(delete("/api/v1/comments/{commentId}/like", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetUsersWhoLikedCommentSuccessfully() throws Exception {
        Integer commentId = 300;
        String email = "usuario@exemplo.com";

        UserInfoResponse user1 = new UserInfoResponse();
        user1.setId(10);
        user1.setUsername("joao");

        UserInfoResponse user2 = new UserInfoResponse();
        user2.setId(20);
        user2.setUsername("maria");

        List<UserInfoResponse> mockList = List.of(user1, user2);

        when(commentService.getUsersWhoLikedComment(commentId)).thenReturn(mockList);

        mockMvc.perform(get("/api/v1/comments/{commentId}/likes", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[1].username").value("maria"));
    }

    @Test
    void testGetUsersWhoLikedCommentWhenCommentIsNotFound() throws Exception {
        Integer commentIdAbsent = 777;
        String email = "usuario@exemplo.com";

        when(commentService.getUsersWhoLikedComment(commentIdAbsent))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/comments/{commentId}/likes", commentIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUsersWhoLikedCommentWhenInternalServerErrorOccurs() throws Exception {
        Integer commentId = 300;
        String email = "usuario@exemplo.com";

        when(commentService.getUsersWhoLikedComment(commentId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/comments/{commentId}/likes", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCountUsersWhoLikedCommentSuccessfully() throws Exception {
        Integer commentId = 400;
        Integer count = 5;
        String email = "usuario@exemplo.com";

        when(commentService.countUsersWhoLikedComment(commentId)).thenReturn(count);

        mockMvc.perform(get("/api/v1/comments/{commentId}/likes/count", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testCountUsersWhoLikedCommentWhenCommentIsNotFound() throws Exception {
        Integer commentIdAbsent = 666;
        String email = "usuario@exemplo.com";

        when(commentService.countUsersWhoLikedComment(commentIdAbsent))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/comments/{commentId}/likes/count", commentIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCountUsersWhoLikedCommentWhenInternalServerErrorOccurs() throws Exception {
        Integer commentId = 400;
        String email = "usuario@exemplo.com";

        when(commentService.countUsersWhoLikedComment(commentId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/comments/{commentId}/likes/count", commentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


}