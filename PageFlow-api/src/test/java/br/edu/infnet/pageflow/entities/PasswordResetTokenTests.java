package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordResetTokenTests {

    private final JwtUtil jwtUtil = new JwtUtil();

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

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
    public Arbitrary<String> validEmails() {
        Arbitrary<String> localPart = Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._%+-")
                .ofMinLength(1)
                .ofMaxLength(20);

        Arbitrary<String> domain = Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-")
                .ofMinLength(1)
                .ofMaxLength(10);

        Arbitrary<String> tld = Arbitraries.of("com", "org", "net", "edu", "gov", "br", "us", "uk");

        return Combinators.combine(localPart, domain, tld)
                .as((l, d, t) -> l + "@" + d + "." + t)
                .filter(email -> EMAIL_PATTERN.matcher(email).matches());
    }

    @Provide
    public Arbitrary<String> validPasswords() {
        Arbitrary<String> letters = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1);
        Arbitrary<String> numbers = Arbitraries.strings().withCharRange('0', '9').ofMinLength(1);
        Arbitrary<String> mixed = Arbitraries.strings()
                .withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
                .ofMinLength(8)
                .ofMaxLength(16);

        return Combinators.combine(letters, numbers, mixed)
                .as((l, n, m) -> l + n + m)
                .filter(pwd -> pwd.length() >= 8 && pwd.length() <= 16);
    }

    @Provide
    public Arbitrary<BlogUser> blogUserProvider() {
        Arbitrary<Integer> userId = validUserId();
        Arbitrary<String> name = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(3).ofMaxLength(20);
        Arbitrary<String> username = validUsername();
        Arbitrary<String> email = validEmails();
        Arbitrary<String> password = validPasswords();
        Arbitrary<BlogUserRoles> role = Arbitraries.of(BlogUserRoles.class);
        Arbitrary<LocalDateTime> createdAt = Arbitraries.just(LocalDateTime.now());
        Arbitrary<LocalDateTime> updatedAt = Arbitraries.just(null);

        return Combinators.combine(userId, name, username, email, password, role, createdAt, updatedAt)
                .as((id, n, u, e, p, r, c, uA) -> {
                    BlogUser user = new BlogUser();
                    user.setId(id);
                    user.setName(n);
                    user.setUsername(u);
                    user.setEmail(e);
                    user.setPassword(p);
                    user.setRole(r);
                    user.setCreatedAt(null);
                    user.setUpdatedAt(uA);
                    return user;
                });
    }

    @Provide
    Arbitrary<String> validJwtToken(@ForAll("blogUserProvider") BlogUser user) {
        return Arbitraries.of(jwtUtil.generateToken(Optional.ofNullable(user)));
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

    @Test
    void testEqualsAndHashCode() {
        BlogUser user = new BlogUser();
        user.setId(1);
        user.setUsername("testuser");

        PasswordResetToken token1 = new PasswordResetToken("abc123", user);
        PasswordResetToken token2 = new PasswordResetToken("abc123", user);

        assertThat(token1).isEqualTo(token2);
        assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
    }

    @Test
    void testNotEqualsDifferentToken() {
        BlogUser user = new BlogUser();
        user.setId(1);

        PasswordResetToken token1 = new PasswordResetToken("abc123", user);
        PasswordResetToken token2 = new PasswordResetToken("xyz999", user);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void testToStringContainsTokenAndDate() {
        PasswordResetToken token = new PasswordResetToken("abc123");
        String toString = token.toString();

        assertThat(toString).contains("abc123");
        assertThat(toString).contains("Expires");
    }

    @Test
    void testUpdateTokenChangesTokenAndExpiry() throws InterruptedException {
        PasswordResetToken token = new PasswordResetToken("initial");
        Date originalExpiry = token.getExpiryDate();

        Thread.sleep(10);

        token.updateToken("updatedToken");

        assertThat(token.getToken()).isEqualTo("updatedToken");
        assertThat(token.getExpiryDate()).isAfter(originalExpiry);
    }

    @Test
    void testEquals_sameInstance() {
        PasswordResetToken token = new PasswordResetToken();
        assertEquals(token, token);
    }

    @Test
    void testEquals_nullObject() {
        PasswordResetToken token = new PasswordResetToken();
        assertNotEquals(token, null);
    }

    @Test
    void testEquals_differentClass() {
        PasswordResetToken token = new PasswordResetToken();
        assertNotEquals(token, "not a token");
    }

    @Test
    void testEquals_expiryDateNullInThis_notInOther() {
        PasswordResetToken token1 = new PasswordResetToken();
        token1.setToken("abc");
        token1.setBlogUser(new BlogUser());

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setToken("abc");
        token2.setBlogUser(new BlogUser());
        token2.setExpiryDate(new Date());

        assertNotEquals(token1, token2);
    }

    @Test
    void testEquals_expiryDateDifferent() {
        PasswordResetToken token1 = new PasswordResetToken();
        token1.setExpiryDate(new Date(1000));
        token1.setToken("abc");
        token1.setBlogUser(new BlogUser());

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setExpiryDate(new Date(2000));
        token2.setToken("abc");
        token2.setBlogUser(new BlogUser());

        assertNotEquals(token1, token2);
    }

    @Test
    void testEquals_tokenNullInThis_notInOther() {
        PasswordResetToken token1 = new PasswordResetToken();
        token1.setExpiryDate(new Date());
        token1.setBlogUser(new BlogUser());

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setExpiryDate(token1.getExpiryDate());
        token2.setToken("xyz");
        token2.setBlogUser(new BlogUser());

        assertNotEquals(token1, token2);
    }

    @Test
    void testEquals_tokenDifferent() {
        PasswordResetToken token1 = new PasswordResetToken();
        token1.setExpiryDate(new Date());
        token1.setToken("abc");
        token1.setBlogUser(new BlogUser());

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setExpiryDate(token1.getExpiryDate());
        token2.setToken("xyz");
        token2.setBlogUser(new BlogUser());

        assertNotEquals(token1, token2);
    }

    @Test
    void testEquals_blogUserNullInThis_notInOther() {
        PasswordResetToken token1 = new PasswordResetToken();
        token1.setExpiryDate(new Date());
        token1.setToken("abc");

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setExpiryDate(token1.getExpiryDate());
        token2.setToken("abc");
        token2.setBlogUser(new BlogUser());

        assertNotEquals(token1, token2);
    }

    @Test
    void testEquals_blogUserDifferent() {
        BlogUser user1 = new BlogUser();
        user1.setId(1);
        BlogUser user2 = new BlogUser();
        user2.setId(2);

        PasswordResetToken token1 = new PasswordResetToken();
        token1.setExpiryDate(new Date());
        token1.setToken("abc");
        token1.setBlogUser(user1);

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setExpiryDate(token1.getExpiryDate());
        token2.setToken("abc");
        token2.setBlogUser(user2);

        assertNotEquals(token1, token2);
    }

    @Test
    void testEquals_allEqual() {
        BlogUser user = new BlogUser();
        user.setId(1);
        Date date = new Date();

        PasswordResetToken token1 = new PasswordResetToken();
        token1.setExpiryDate(date);
        token1.setToken("abc");
        token1.setBlogUser(user);

        PasswordResetToken token2 = new PasswordResetToken();
        token2.setExpiryDate(date);
        token2.setToken("abc");
        token2.setBlogUser(user);

        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    void testHashCode_withNullFields() {
        PasswordResetToken token = new PasswordResetToken();
        assertDoesNotThrow(token::hashCode);
    }

}
