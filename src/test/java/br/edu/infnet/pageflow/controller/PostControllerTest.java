package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.UserInfoResponse;
import br.edu.infnet.pageflow.entities.Author;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.entities.Category;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.repository.PostLikeRelationRepository;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.PostService;
import br.edu.infnet.pageflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    private UserService userService;

    @MockBean
    private PostLikeRelationRepository postLikeRelationRepository;


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


    @Test
    void testAddLikePostSuccessfully() throws Exception {
        Integer postId = 100;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);

        when(postService.addLikePost(postId, userMock)).thenReturn(true);

        mockMvc.perform(post("/api/v1/posts/{postId}/like", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testUserCannotLikePostTwice() throws Exception {
        Integer postId = 100;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);

        when(postService.addLikePost(postId, userMock)).thenReturn(true);

        mockMvc.perform(post("/api/v1/posts/{postId}/like", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        when(postService.addLikePost(postId, userMock)).thenReturn(false);


        mockMvc.perform(post("/api/v1/posts/{postId}/like", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddLikePostWhenPostIsNotFound() throws Exception {
        Integer postIdAbsent = 999;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new EntityNotFoundException()).when(postService).addLikePost(postIdAbsent, userMock);

        mockMvc.perform(post("/api/v1/posts/{postId}/like", postIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddLikePostWhenInternalServerErrorOccurs() throws Exception {
        Integer postId = 100;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new RuntimeException("Erro inesperado")).when(postService).addLikePost(postId, userMock);

        mockMvc.perform(post("/api/v1/posts/{postId}/like", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRemoveLikePostSuccessfully() throws Exception {
        Integer postId = 200;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doNothing().when(postService).removeLikePost(postId, userMock.getId());

        mockMvc.perform(delete("/api/v1/posts/{postId}/like", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveLikePostWhenPostIsNotFound() throws Exception {
        Integer postIdAbsent = 888;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new EntityNotFoundException()).when(postService).removeLikePost(postIdAbsent, userMock.getId());

        mockMvc.perform(delete("/api/v1/posts/{postId}/like", postIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveLikePostWhenInternalServerErrorOccurs() throws Exception {
        Integer postId = 200;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(userMock);
        doThrow(new RuntimeException("Unexpected error")).when(postService).removeLikePost(postId, userMock.getId());

        mockMvc.perform(delete("/api/v1/posts/{postId}/like", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetUsersWhoLikedPostSuccessfully() throws Exception {
        Integer postId = 300;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        UserInfoResponse user1 = new UserInfoResponse();
        user1.setId(10);
        user1.setUsername("joao");

        UserInfoResponse user2 = new UserInfoResponse();
        user2.setId(20);
        user2.setUsername("maria");

        List<UserInfoResponse> mockList = List.of(user1, user2);

        when(postService.getUsersWhoLikedPost(postId)).thenReturn(mockList);

        mockMvc.perform(get("/api/v1/posts/{postId}/likes", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[1].username").value("maria"));
    }

    @Test
    void testGetUsersWhoLikedPostWhenPostIsNotFound() throws Exception {
        Integer postIdAbsent = 777;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(postService.getUsersWhoLikedPost(postIdAbsent))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/posts/{postId}/likes", postIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUsersWhoLikedPostWhenInternalServerErrorOccurs() throws Exception {
        Integer postId = 300;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(postService.getUsersWhoLikedPost(postId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/posts/{postId}/likes", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCountUsersWhoLikedPostSuccessfully() throws Exception {
        Integer postId = 400;
        Integer count = 5;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(postService.countUsersWhoLikedPost(postId)).thenReturn(count);

        mockMvc.perform(get("/api/v1/posts/{postId}/likes/count", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testCountUsersWhoLikedPostWhenPostIsNotFound() throws Exception {
        Integer postIdAbsent = 666;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(postService.countUsersWhoLikedPost(postIdAbsent))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/posts/{postId}/likes/count", postIdAbsent)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCountUsersWhoLikedPostWhenInternalServerErrorOccurs() throws Exception {
        Integer postId = 400;
        String email = "usuario@exemplo.com";

        BlogUser userMock = new BlogUser();
        userMock.setId(42);
        userMock.setEmail(email);

        when(postService.countUsersWhoLikedPost(postId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/posts/{postId}/likes/count", postId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt.claim("sub", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}