package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.PostTagRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostTagRelationRepositoryTest {

    @Autowired
    private PostTagRelationRepository repository;

    @Test
    void testSaveAndFind() {
    }
}
