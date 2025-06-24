package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testGetAllCommentsReturnsSavedComment() {
        Comment comment = new Comment();
        comment.setContent("Comentário de teste");
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        Collection<Comment> comments = commentRepository.getAllComments();

        assertFalse(comments.isEmpty());
        assertTrue(comments.stream().anyMatch(c -> "Comentário de teste".equals(c.getContent())));
    }
}
