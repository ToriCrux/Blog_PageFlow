package br.edu.infnet.pageflow.entities;

import net.jqwik.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CommentTests {

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

        Category category = new Category();
        category.setId(userId);

        assertNotNull(category);
        assertSame(category.getId(), userId);
    }

    @Property
    void testContentMustBeValidText(@ForAll("content") String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setApproved(true);
        comment.setUpdatedAt(LocalDateTime.now());

        assertNotNull(comment);
        assertSame(comment.getContent(), content);
        assertThat(comment.isApproved()).isTrue();
        assertThat(comment.getContent().length()).isLessThanOrEqualTo(500);
    }

    @Property
    void testUpdateAtMustBeValidDate(@ForAll("content") String content) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setApproved(true);
        comment.setUpdatedAt(LocalDateTime.now());

        assertNotNull(comment);
        assertThat(comment.getUpdatedAt()).isNotNull();
        assertThat(comment.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

    }
}
