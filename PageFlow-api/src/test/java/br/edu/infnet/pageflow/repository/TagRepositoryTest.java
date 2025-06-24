package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository repository;

    @Test
    void testSaveAndFind() {
        Tag tag = new Tag();
        tag.setName("Java");
        repository.save(tag);

        assertTrue(repository.findAll().iterator().hasNext());
    }
}
