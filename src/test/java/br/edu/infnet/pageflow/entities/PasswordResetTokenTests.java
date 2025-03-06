package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import net.jqwik.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordResetTokenTests {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Provide
    public Arbitrary<Integer> validUserId() {
        return Arbitraries.integers()
                .between(1, 2147483647);
    }

    @Provide
    public Arbitrary<String> validUsername() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(5)
                .ofMaxLength(16)
                .filter(username -> !username.isBlank());
    }

    @Provide
    public Arbitrary<String> validJwtToken(@ForAll("validUsername") String username) {
        return Arbitraries.of(jwtUtil.generateToken(username));
    }

    @Property
    void testIdMustBeInteger(@ForAll("validUserId") Integer userId, @ForAll("validJwtToken") String token) {

        PasswordResetToken resetToken = new PasswordResetToken(token);
        resetToken.setId(userId);

        assertNotNull(resetToken);
        assertEquals(resetToken.getId(), userId);
        assertThat(resetToken.getId()).isInstanceOf(Integer.class);
    }

    @Property
    void testJwtTokenMustBeValid(@ForAll("validJwtToken") String token) {

        BlogUser user = new BlogUser();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        resetToken.setToken(token);

        assertNotNull(resetToken);
        assertSame(resetToken.getToken(), token);
    }

    @Property
    void testBlogUserShouldBeAuthor(@ForAll("validJwtToken") String token) {

        Author author = new Author();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setBlogUser(author);
        resetToken.setToken(token);

        assertNotNull(resetToken);
        assertSame(resetToken.getToken(), token);
    }

}
