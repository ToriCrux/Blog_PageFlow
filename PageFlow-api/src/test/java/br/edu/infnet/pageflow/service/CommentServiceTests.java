package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.dto.CommentRequest;
import br.edu.infnet.pageflow.entities.Comment;
import br.edu.infnet.pageflow.entities.Post;
import br.edu.infnet.pageflow.entities.PostCommentRelation;
import br.edu.infnet.pageflow.repository.CommentRepository;
import br.edu.infnet.pageflow.repository.PostCommentRelationRepository;
import br.edu.infnet.pageflow.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @Mock private CommentRepository commentRepository;
    @Mock private PostCommentRelationRepository postCommentRelationRepository;
    @Mock private PostRepository postRepository;

    @InjectMocks private CommentService commentService;

    private Comment comment;
    private Post post;

    @BeforeEach
    void setup() {
        comment = new Comment();
        comment.setId(1);
        comment.setContent("Test comment");
        comment.setApproved(true);

        post = new Post();
        post.setId(1);
        post.setTitle("Test Post");
    }

    @Test
    void testGetComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        Collection<Comment> result = commentService.getComments();
        assertEquals(1, result.size());
    }

    @Test
    void testGetCommentsByPost() {
        PostCommentRelation rel = new PostCommentRelation();
        rel.setComment(comment);

        when(postRepository.getPostById(1)).thenReturn(Optional.of(post));
        when(postCommentRelationRepository.findAllByPostId(1)).thenReturn(List.of(rel));

        Collection<Comment> result = commentService.getCommentsByPost(1);
        assertEquals(1, result.size());
        assertEquals(comment, result.iterator().next());
    }

    @Test
    void testGetCommentsByPost_PostNotFound() {
        when(postRepository.getPostById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> commentService.getCommentsByPost(99));
    }

    @Test
    void testCreateComment() {
        CommentRequest req = new CommentRequest();
        req.setContent("New Comment");
        req.setApproved(false);

        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment created = commentService.createComment(req);

        assertEquals("New Comment", created.getContent());
        assertFalse(created.isApproved());
    }

    @Test
    void testUpdateComment_Exists() {
        CommentRequest req = new CommentRequest();
        req.setContent("Updated Content");
        req.setApproved(true);

        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment updated = commentService.updateComment(1, req);

        assertEquals("Updated Content", updated.getContent());
        assertTrue(updated.isApproved());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testUpdateComment_NotFound() {
        when(commentRepository.findById(999)).thenReturn(Optional.empty());
        Comment updated = commentService.updateComment(999, new CommentRequest());
        assertNull(updated);
    }

    @Test
    void testDeleteComment_Success() {
        when(commentRepository.existsById(1)).thenReturn(true);
        commentService.deleteComment(1);
        verify(commentRepository).deleteById(1);
    }

    @Test
    void testDeleteComment_NotFound() {
        when(commentRepository.existsById(1)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> commentService.deleteComment(1));
    }
}
