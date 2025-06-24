package br.edu.infnet.pageflow;

import br.edu.infnet.pageflow.dto.*;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.*;
import br.edu.infnet.pageflow.service.*;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import br.edu.infnet.pageflow.utils.PostStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.ApplicationArguments;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.mockito.Mockito.*;

class DataLoaderTests {

    private CategoryService categoryService;
    private CommentService commentService;
    private PostRepository postRepository;
    private PostService postService;
    private UserService userService;
    private UserRepository userRepository;
    private TagService tagService;
    private CommentRepository commentRepository;
    private PostCommentRelationRepository postCommentRelationRepository;

    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        commentService = mock(CommentService.class);
        postRepository = mock(PostRepository.class);
        postService = mock(PostService.class);
        userService = mock(UserService.class);
        userRepository = mock(UserRepository.class);
        tagService = mock(TagService.class);
        commentRepository = mock(CommentRepository.class);
        postCommentRelationRepository = mock(PostCommentRelationRepository.class);

        dataLoader = Mockito.spy(new DataLoader());

        // Injetar manualmente os mocks
        dataLoader.categoryService = categoryService;
        dataLoader.commentService = commentService;
        dataLoader.postRepository = postRepository;
        dataLoader.postService = postService;
        dataLoader.userService = userService;
        dataLoader.userRepository = userRepository;
        dataLoader.tagService = tagService;
        dataLoader.commentRepository = commentRepository;
        dataLoader.postCommentRelationRepository = postCommentRelationRepository;
    }

    @Test
    void testRunWithValidData() throws Exception {
        String mockData = String.join("\n",
                "author;1;João Silva;joaosilva;joao@email.com;123",
                "category;2;Tecnologia",
                "comment;3;Ótimo post!",
                "post;4;Título;Conteúdo;1;2;DRAFT",
                "postcommentrelation;5;4;3",
                "invalid;data;line"
        );

        // Criar post e comentário mockados para relacionamento
        Post mockPost = new Post();
        mockPost.setId(4);
        Comment mockComment = new Comment();
        mockComment.setId(3);

        doReturn(new ByteArrayInputStream(mockData.getBytes()))
                .when(dataLoader).getClassLoaderResourceAsStream();

        when(postRepository.getPostById(4)).thenReturn(Optional.of(mockPost));
        when(commentRepository.findById(3)).thenReturn(Optional.of(mockComment));

        // Executar
        dataLoader.run(mock(ApplicationArguments.class));

        // Verificações
        verify(userService).createUser(any(SignupRequest.class));
        verify(categoryService).createCategory("Tecnologia");
        verify(commentService).createComment(any(CommentRequest.class));
        verify(postService).createPost(any(PostRequest.class));
        verify(postCommentRelationRepository).save(any(PostCommentRelation.class));
    }
}
