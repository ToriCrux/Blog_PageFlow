package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.PostCommentRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostCommentRelationRepositoryTest {

    @Autowired
    private PostCommentRelationRepository repository;

    @Test
    void testSaveAndFind() {
    }
}
