package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.dto.PostRequest;
import net.jqwik.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PostTests {

    @Provide
    public Arbitrary<Integer> validId() {
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
    void testPostMustBeCorrectlyCreated(@ForAll("postTitles") String title,
                                        @ForAll("content") String content,
                                        @ForAll("validId") Integer authorId) {
        Author author = new Author();
        author.setId(authorId);
        author.setBio("Lorem ipsum dolum sit amet");

        Post newPost = new Post(title, content, author);

        assertNotNull(newPost);
        assertThat(newPost.getTitle()).isEqualTo(title);
        assertThat(newPost.getContent()).isEqualTo(content);
        assertThat(newPost.getAuthor()).isEqualTo(author);
        assertThat(newPost).isInstanceOf(Post.class);
    }

    @Property
    void testPostIdMustBeInteger(@ForAll("validId") Integer userId) {

        Author author = new Author();
        author.setId(userId);
        author.setBio("Lorem ipsum dolum sit amet");

        Post newPost = new Post();
        newPost.setId(userId);
        newPost.setAuthor(author);

        assertNotNull(newPost);
        assertSame(newPost.getId(), userId);
        assertThat(newPost.getAuthor()).isInstanceOf(Author.class);
    }

    @Property
    void testPostMustHaveAValidTitle(@ForAll("postTitles") String postTitles) {
        Post newPost = new Post();
        newPost.setTitle(postTitles);

        assertNotNull(postTitles);
        assertSame(newPost.getTitle(), postTitles);
        assertThat(newPost.getTitle().length()).isLessThanOrEqualTo(60);
    }

    @Property
    void testPostContentMustBeValidText(@ForAll("content") String content) {
        Post newPost = new Post();
        newPost.setContent(content);

        assertNotNull(newPost);
        assertSame(newPost.getContent(), content);
        assertThat(newPost.getContent().length()).isLessThanOrEqualTo(500);
    }
}
