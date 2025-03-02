package br.edu.infnet.pageflow.model;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthorTests {

    @Property
    void testAuthorMustBeValid(@ForAll String name,
                               @ForAll String email,
                               @ForAll String username,
                               @ForAll String password,
                               @ForAll String bio) {
        Author author = new Author();
        author.setFirstName(name);
        author.setUsername(username);
        author.setEmail(email);
        author.setPassword(password);
        author.setBio(bio);

        assertThat(author.getFirstName()).isEqualTo(name);
    }
}
