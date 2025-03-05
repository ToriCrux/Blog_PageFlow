package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.Author;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Property
    public void testFindAll(@ForAll @IntRange(min = 0, max = 1000) int authorId) {
        Author author = mock(Author.class);
        userRepository.save(author);

        assertNotNull(author);

    }
}
