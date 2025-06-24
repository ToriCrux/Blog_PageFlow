package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository repository;

    @Test
    void testSaveAndFind() {
    }
}
