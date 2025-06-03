package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
}