package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.entities.Author;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private PostCommentRelationRepository postCommentRelationRepository;

    @Test
    void testGetAllPosts() throws Exception {
        Author author = new Author();
        List<Post> posts = List.of(new Post("Um novo post", "lorem ipsum dolun sit amet", author));

        when(postService.getPosts()).thenReturn(posts);

        mockMvc.perform(get("/api/v1/posts")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Um novo post"));
    }

    @Test
    void testCreatePost() throws Exception {

        Author author = new Author();
        author.setId(1);

        Post post = new Post("Um novo post", "lorem ipsum dolun sit amet", author);
        Category category = new Category();
        category.setId(1);
        post.setCategory(category);

        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(post.getTitle());
        postRequest.setContent(post.getContent());
        postRequest.setAuthorId(post.getAuthor().getId());
        postRequest.setCategoryId(post.getCategory().getId());

        when(postService.createPost(postRequest)).thenReturn(post);

        mockMvc.perform(post("/api/v1/posts/new")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)  // Define o tipo do conteúdo
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdatePost() throws Exception {

        Author author = new Author();
        author.setId(2);

        Category category = new Category();
        category.setId(3);
        category.setName("test");

        Post updatedPost = new Post("Um novo post editado", "lorem ipsum dolun sit amet", author);
        updatedPost.setId(1);
        updatedPost.setCategory(category);

        when(postService.updatePost(eq(1), any(Post.class))).thenReturn(updatedPost);

        mockMvc.perform(put("/api/v1/posts/{id}", updatedPost.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Um novo post editado"));
    }

    @Test
    void testDeletePost() throws Exception {
        Author author = new Author();
        author.setId(1);

        Post post = new Post("Um novo post", "lorem ipsum dolun sit amet", author);
        Category category = new Category();
        category.setId(1);
        post.setCategory(category);

        doNothing().when(postService).deletePost(post.getId());

        mockMvc.perform(delete("/api/v1/posts/{id}", category.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}