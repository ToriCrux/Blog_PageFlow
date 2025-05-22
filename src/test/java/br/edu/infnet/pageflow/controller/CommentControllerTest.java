package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CommentService;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void testGetCommentsByParentCommentSuccessfully() throws Exception {
        int parentId = 10;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        Comment parentComment = new Comment();
        parentComment.setId(parentId);

        Comment comment1 = new Comment();
        comment1.setId(1);
        comment1.setContent("Comentário 1");
        comment1.setParentComment(parentComment);

        Comment comment2 = new Comment();
        comment2.setId(2);
        comment2.setContent("Comentário 2");
        comment2.setParentComment(parentComment);

        List<Comment> comments = List.of(comment1, comment2);

        when(commentService.getCommentsByParentComment(parentId)).thenReturn(comments);

        mockMvc.perform(get("/api/v1/comments/parentComment/{parentCommentId}", parentId)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Comentário 1"))
                .andExpect(jsonPath("$[0].parentComment.id").value(parentId))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content").value("Comentário 2"))
                .andExpect(jsonPath("$[1].parentComment.id").value(parentId));
    }

    @Test
    void testGetCommentsByParentCommentWhenPostIsNotFound() throws Exception {
        int parentId = 99;
        String email = "usuario@exemplo.com";

        when(commentService.getCommentsByParentComment(parentId))
                .thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/comments/parentComment/{parentCommentId}", parentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCommentsByParentCommentWhenInternalServerErrorOccurs() throws Exception {
        int parentId = 1;
        String email = "usuario@exemplo.com";

        when(commentService.getCommentsByParentComment(parentId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/comments/parentComment/{parentCommentId}", parentId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }



}