package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.utils.BlogUserRoles;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTests {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Provide
    Arbitrary<String> validUsername() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(5)
                .ofMaxLength(16)
                .filter(username -> !username.isBlank());
    }

    @Provide
    Arbitrary<String> validEmails() {
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
    Arbitrary<String> validPasswords() {
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
    Arbitrary<BlogUserRoles> blogUserRoles() {
        return Arbitraries.of(BlogUserRoles.values());
    }

    @Property
    void testNameMustBeValid(@ForAll @NotBlank String name) {

        SignupRequest signupRequest = new SignupRequest(name, "username", "email@example.com", "password123", BlogUserRoles.AUTHOR);

        assertEquals(name, signupRequest.getName());
    }

    @Property
    void testNameMustBeNotNull(@ForAll("validUsername") String name) {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName(name);

        assertNotNull(signupRequest.getName());
        assertFalse(signupRequest.getName().isEmpty(), "Username cannot be empty");
    }

    @Property
    void testUsernameMustBeValidAndNotNull(@ForAll("validUsername") String username) {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);

        assertNotNull(signupRequest.getUsername());
        assertFalse(signupRequest.getUsername().isEmpty(), "Username cannot be empty");
        assertEquals(username, signupRequest.getUsername());
    }

    @Property
    void testEmailMustBeValidAndNotNull(@ForAll("validEmails") String email) {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);

        assertTrue(EMAIL_PATTERN.matcher(signupRequest.getEmail()).matches(), "Email must have a valid format");
        assertNotNull(signupRequest.getEmail());
        assertEquals(email, signupRequest.getEmail());
    }

    @Property
    void testPasswordMustBeValidAndNotNull(@ForAll("validPasswords") String password) {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setPassword(password);

        assertTrue(signupRequest.getPassword().length() >= 8, "Password must have at least 8 characters");
        assertTrue(signupRequest.getPassword().matches(".*[a-zA-Z].*"), "Password must contain at least one letter");
        assertTrue(signupRequest.getPassword().matches(".*[0-9].*"), "Password must contain at least one number");

        assertNotNull(signupRequest.getPassword());
        assertEquals(password, signupRequest.getPassword());
    }

    @Property
    void testRoleMustBeValidAndNotNull(@ForAll("blogUserRoles") BlogUserRoles role) {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(role);

        assertNotNull(role);
        assertEquals(role, signupRequest.getRole());
    }
}
