package br.edu.infnet.pageflow.dto;

import net.jqwik.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PostRequestDtoTests {

    @Provide
    public Arbitrary<Integer> validAuthorId() {
        return Arbitraries.integers()
                .between(1, 2147483647);
    }

    @Provide
    public Arbitrary<String> postTitles() {
        return Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ")
                .ofMinLength(5)
                .ofMaxLength(60) // Garante que o título não ultrapasse 60 caracteres
                .map(String::trim) // Remove espaços extras no início e no fim
                .filter(title -> !title.isBlank() && title.length() <= 60);
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
    void testPostTitleMustBeValidText(@ForAll("postTitles") String title) {

        PostRequest newPost = new PostRequest();
        newPost.setTitle(title);

        assertNotNull(title);
        assertSame(newPost.getTitle(), title);
        assertThat(newPost.getTitle().length()).isLessThanOrEqualTo(60);
    }

    @Property
    void testContentMustBeValidText(@ForAll("content") String content) {

        PostRequest newPost = new PostRequest();
        newPost.setContent(content);

        assertNotNull(newPost);
        assertSame(newPost.getContent(), content);
        assertThat(newPost.getContent().length()).isLessThanOrEqualTo(500);

    }

    @Property
    void testPostMustHaveAnOwner(@ForAll("postTitles") String title,
                                 @ForAll("content") String content,
                                 @ForAll("validAuthorId") Integer authorId) {

        PostRequest newPost = new PostRequest();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setAuthorId(authorId);

        assertNotNull(newPost);
        assertSame(newPost.getAuthorId(), authorId);
        assertThat(newPost).isInstanceOf(PostRequest.class);

    }
}
