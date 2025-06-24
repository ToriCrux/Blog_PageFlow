package br.edu.infnet.pageflow.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostCommentRelationTests {

    @Test
    public void testPostCommentRelationFields() {
        PostCommentRelation relation = new PostCommentRelation();
        Comment comment = new Comment();
        Post post = new Post();

        relation.setId(10);
        relation.setPost(post);
        relation.setComment(comment);

        assertEquals(10, relation.getId());
        assertEquals(post, relation.getPost());
        assertEquals(comment, relation.getComment());
    }
}

