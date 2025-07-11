package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.utils.BlogUserRoles;
import net.jqwik.api.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseDtoTests {

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
    void testLoginResponseDto(@ForAll("validJwtToken") String jwtToken) {

        LoginResponse loginResponse = new LoginResponse(jwtToken);

        assertEquals(jwtToken, loginResponse.jwt());
        assertNotNull(loginResponse.jwt());
        assertFalse(loginResponse.jwt().isBlank());
        assertNotNull(jwtUtil.extractUsername(loginResponse.jwt()));
        assertFalse(jwtUtil.extractExpiration(jwtToken).before(new Date()));
    }
}
