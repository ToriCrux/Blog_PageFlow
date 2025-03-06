package br.edu.infnet.pageflow.entities;

import net.jqwik.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PostTests {

    @Provide
    public Arbitrary<Integer> validUserId() {
        return Arbitraries.integers()
                .between(1, 2147483647);
    }

    @Provide
    public Arbitrary<String> content() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(20)
                .ofMaxLength(500)
                .filter(comment -> !comment.isBlank());
    }

    @Property
    void testIdMustBeInteger(@ForAll("validUserId") Integer userId) {

        Post newpost = new Post();
        newpost.setId(userId);

        assertNotNull(newpost);
        assertSame(newpost.getId(), userId);
    }

    @Property
    void testContentMustBeValidText(@ForAll("content") String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setApproved(true);

        assertNotNull(comment);
        assertSame(comment.getContent(), content);
        assertThat(comment.isApproved()).isTrue();
        assertThat(comment.getContent().length()).isLessThanOrEqualTo(500);
    }
}
