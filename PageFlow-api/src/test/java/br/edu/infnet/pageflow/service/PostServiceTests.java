package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.PostRequest;
import br.edu.infnet.pageflow.dto.PostResponse;
import br.edu.infnet.pageflow.entities.*;
import br.edu.infnet.pageflow.repository.*;
import br.edu.infnet.pageflow.utils.PostStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TagRepository tagRepository;
    @Mock private PostTagRelationRepository postTagRelationRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private PostCommentRelationRepository postCommentRelationRepository;

    @InjectMocks private PostService postService;

    private Post post;
    private Author author;
    private Category category;
    private Tag tag;
    private Comment comment;

    @BeforeEach
    void setup() {
        author = new Author();
        author.setId(1);

        post = new Post();
        post.setId(1);
        post.setTitle("Title");
        post.setContent("Content");
        post.setAuthor(author);
        post.setStatus(PostStatus.DRAFT);

        category = new Category();
        category.setId(1);
        category.setName("Tech");

        tag = new Tag();
        tag.setId(1);
        tag.setName("Java");

        comment = new Comment();
        comment.setId(1);
        comment.setContent("Nice post!");
        comment.setApproved(true);
    }

    @Test
    void testCreatePostWithCategory() {
        PostRequest request = new PostRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setAuthorId(1);
        request.setStatus(PostStatus.PUBLISHED);
        request.setCategoryId(1);

        when(userRepository.findAuthorById(1)).thenReturn(author);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));

        Post result = postService.createPost(request);

        assertEquals("Test Title", result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(category, result.getCategory());
    }

    @Test
    void testFindByIdSuccess() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postCommentRelationRepository.findAllByPostId(1)).thenReturn(List.of());

        Post found = postService.findById(1);
        assertEquals(post.getId(), found.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> postService.findById(1));
    }

    @Test
    void testGetPosts() {
        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(post));
        when(postCommentRelationRepository.findAllByPostId(1)).thenReturn(List.of());

        Collection<Post> posts = postService.getPosts();
        assertEquals(1, posts.size());
    }

    @Test
    void testGetPostWithComments() {
        PostCommentRelation rel = new PostCommentRelation();
        rel.setComment(comment);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postCommentRelationRepository.findAllByPostId(1)).thenReturn(List.of(rel));

        PostResponse response = postService.getPostWithComments(1);
        assertEquals(1, response.getComments().size());
    }

    @Test
    void testUpdatePost() {
        Post updated = new Post();
        updated.setTitle("Updated");
        updated.setContent("Updated Content");
        updated.setStatus(PostStatus.PUBLISHED);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.updatePost(1, updated);
        assertEquals("Updated", result.getTitle());
    }

    @Test
    void testUpdatePostNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> postService.updatePost(1, new Post()));
    }

    @Test
    void testDeletePost() {
        when(postRepository.existsById(1)).thenReturn(true);
        postService.deletePost(1);
        verify(postRepository).deleteById(1);
    }

    @Test
    void testDeletePostNotFound() {
        when(postRepository.existsById(1)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> postService.deletePost(1));
    }

    @Test
    void testAddCommentToPost() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(postCommentRelationRepository.findAllByPostId(1)).thenReturn(List.of());

        PostResponse response = postService.addCommentToPost(1, 1);
        assertEquals(0, response.getComments().size());
    }

    @Test
    void testDeleteCommentFromPost() {
        PostCommentRelation rel = new PostCommentRelation();
        rel.setPost(post);
        rel.setComment(comment);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(postCommentRelationRepository.findByPostIdAndCommentId(1, 1)).thenReturn(Optional.of(rel));

        postService.deleteCommentFromPost(1, 1);
        verify(postCommentRelationRepository).delete(rel);
    }

    @Test
    void testAddTagToPost() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));

        PostTagRelation rel = new PostTagRelation();
        rel.setPost(post);
        rel.setTag(tag);

        when(postTagRelationRepository.save(any())).thenReturn(rel);

        Post result = postService.addTagToPost(1, 1);
        assertEquals(post, result);
    }

    @Test
    void testRemoveTagFromPost() {
        PostTagRelation rel = new PostTagRelation();
        rel.setPost(post);
        rel.setTag(tag);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        when(postTagRelationRepository.findByPostAndTag(post, tag)).thenReturn(Optional.of(rel));

        postService.removeTagFromPost(1, 1);
        verify(postTagRelationRepository).delete(rel);
    }

    @Test
    void testFindByCategoryName() {
        when(categoryRepository.findByName("Tech")).thenReturn(category);
        when(postRepository.findByCategoryId(1)).thenReturn(List.of(post));

        Collection<Post> posts = postService.findByCategoryName("Tech");
        assertEquals(1, posts.size());
    }

    @Test
    void testFindByCategoryNameNull() {
        when(categoryRepository.findByName("None")).thenReturn(null);
        assertNull(postService.findByCategoryName("None"));
    }

    @Test
    void testGetDraftPost() {
        when(postRepository.getDraftPostByAuthor(1)).thenReturn(Optional.of(post));
        Optional<Post> result = postService.getDraftPost(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindByTagName() {
        when(tagRepository.findByName("Java")).thenReturn(tag);
        when(postTagRelationRepository.findByTagId(1)).thenReturn(List.of(post));

        Collection<Post> posts = postService.findByTagName("Java");
        assertEquals(1, posts.size());
    }

    @Test
    void testFindByTagNameNull() {
        when(tagRepository.findByName("None")).thenReturn(null);
        assertNull(postService.findByTagName("None"));
    }

    @Test
    void testGetCommentById() {
        when(postCommentRelationRepository.findCommentById(1)).thenReturn(comment);
        Comment result = postService.getCommentById(1);
        assertEquals(comment, result);
    }

    @Test
    void testSavePost() {
        postService.savePost(post);
        verify(postRepository).save(post);
    }

    @Test
    void testGetCommentsByPostId() {
        PostCommentRelation rel = new PostCommentRelation();
        rel.setComment(comment);

        when(postCommentRelationRepository.findAllByPostId(1)).thenReturn(List.of(rel));
        List<Comment> comments = postService.getCommentsByPostId(1);

        assertEquals(1, comments.size());
        assertEquals(comment, comments.getFirst());
    }
}
