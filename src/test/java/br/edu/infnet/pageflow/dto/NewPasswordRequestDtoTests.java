package br.edu.infnet.pageflow.dto;

import net.jqwik.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NewPasswordRequestDtoTests {

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
    Arbitrary<String> validToken() {
        return Arbitraries.create(UUID::randomUUID).map(UUID::toString);
    }

    @Property
    void testValidOldPassword(@ForAll("validPasswords") String oldPassword) {

        NewPasswordRequest passwordRequest = new NewPasswordRequest(oldPassword, "4da68d2c-0c63-49e0-ac1c-d923a6c298da", "password123");

        assertTrue(passwordRequest.getOldPassword().length() >= 8, "Password must have at least 8 characters");
        assertTrue(passwordRequest.getOldPassword().matches(".*[a-zA-Z].*"), "Password must contain at least one letter");
        assertTrue(passwordRequest.getOldPassword().matches(".*[0-9].*"), "Password must contain at least one number");

        assertNotEquals(passwordRequest.getNewPassword(), passwordRequest.getOldPassword());
        assertEquals(oldPassword, passwordRequest.getOldPassword());
    }

    @Property
    void testValidNewPassword(@ForAll("validPasswords") String newPassword) {

        NewPasswordRequest passwordRequest = new NewPasswordRequest("password123", "4da68d2c-0c63-49e0-ac1c-d923a6c298da", newPassword);

        assertTrue(passwordRequest.getNewPassword().length() >= 8, "Password must have at least 8 characters");
        assertTrue(passwordRequest.getNewPassword().matches(".*[a-zA-Z].*"), "Password must contain at least one letter");
        assertTrue(passwordRequest.getNewPassword().matches(".*[0-9].*"), "Password must contain at least one number");

        assertEquals(newPassword, passwordRequest.getNewPassword());
    }

    @Property
    void testValidToken(@ForAll("validToken") String token) {
        NewPasswordRequest passwordRequest = new NewPasswordRequest("password123", token, "newPassword");

        assertNotNull(passwordRequest.getToken());
        assertTrue(passwordRequest.getToken().matches("^[a-f0-9\\-]{36}$"), "Token must be a valid UUID format");
    }
}
