package br.edu.infnet.pageflow.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostTagRelationTests {

    @Test
    public void testPostTagRelationFields() {
        PostTagRelation relation = new PostTagRelation();
        Tag tag = new Tag();
        Post post = new Post();

        relation.setId(11);
        relation.setPost(post);
        relation.setTag(tag);

        assertEquals(11, relation.getId());
        assertEquals(post, relation.getPost());
        assertEquals(tag, relation.getTag());
    }
}

