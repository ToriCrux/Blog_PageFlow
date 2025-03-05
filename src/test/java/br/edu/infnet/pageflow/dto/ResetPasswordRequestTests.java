package br.edu.infnet.pageflow.dto;

import net.jqwik.api.*;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class ResetPasswordRequestTests {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


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

    @Property
    void testEmailMustBeValid(@ForAll("validEmails") String email) {

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail(email);

        assertNotNull(resetPasswordRequest.getEmail());
        assertEquals(email, resetPasswordRequest.getEmail());
    }

    @Property
    void testEmailMustBeValidDomain(@ForAll("validEmails") String email) {

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(email);

        assertNotNull(resetPasswordRequest.getEmail());
        assertTrue(EMAIL_PATTERN.matcher(resetPasswordRequest.getEmail()).matches(), "Email must have a valid format");
    }
}
