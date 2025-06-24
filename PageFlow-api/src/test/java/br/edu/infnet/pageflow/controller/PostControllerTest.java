package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.TestSecurityConfig;
import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.PostResponse;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.*;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.CommentService;
import br.edu.infnet.pageflow.service.PostService;
import br.edu.infnet.pageflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthUserDetailsService authUserDetailsService;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserService userService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private  CategoryRepository categoryRepository;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private PostTagRelationRepository postTagRelationRepository;

    @MockBean
    private CommentRepository commentRepository;

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

    @Test
    void testUpdateComment() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Updated content");
        commentRequest.setApproved(true);

        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setId(2);

        when(postService.findById(1)).thenReturn(post);
        when(postService.getCommentById(2)).thenReturn(comment);

        mockMvc.perform(put("/api/v1/posts/1/2")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteComment() throws Exception {
        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setId(2);

        when(postService.findById(1)).thenReturn(post);
        when(postService.getCommentById(2)).thenReturn(comment);
        doNothing().when(postService).deleteCommentFromPost(1, 2);

        mockMvc.perform(delete("/api/v1/posts/1/2").with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddComment() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Nice post!");
        commentRequest.setApproved(true);

        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setId(2);
        PostResponse response = new PostResponse();

        when(postService.findById(1)).thenReturn(post);
        when(commentService.createComment(any(CommentRequest.class))).thenReturn(comment);
        when(postService.addCommentToPost(1, 2)).thenReturn(response);

        mockMvc.perform(post("/api/v1/posts/1/comment")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetDraftPost() throws Exception {
        BlogUser user = new BlogUser();
        user.setId(1);
        Optional<Post> draft = Optional.of(new Post());

        when(userService.getUserByEmail(any())).thenReturn(user);
        when(postService.getDraftPost(1)).thenReturn(draft);

        mockMvc.perform(get("/api/v1/posts/draft").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveTag() throws Exception {
        doNothing().when(postService).removeTagFromPost(1, 2);

        mockMvc.perform(delete("/api/v1/posts/1/tags/2").with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPostsByCategory() throws Exception {
        when(postService.findByCategoryName("tech")).thenReturn(List.of(new Post()));

        mockMvc.perform(get("/api/v1/posts/search/tech").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPostsByTag() throws Exception {
        when(postService.findByTagName("java")).thenReturn(List.of(new Post()));

        mockMvc.perform(get("/api/v1/posts/tagSearch/java").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void testAddTag() throws Exception {
        Post post = new Post();
        post.setId(1);
        when(postService.addTagToPost(1, 2)).thenReturn(post);

        mockMvc.perform(post("/api/v1/posts/1/tags/2").with(jwt()))
                .andExpect(status().isOk());
    }
}