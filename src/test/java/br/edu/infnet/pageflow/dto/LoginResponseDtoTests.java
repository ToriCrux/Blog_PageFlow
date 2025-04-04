package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import net.jqwik.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseDtoTests {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Provide
    Arbitrary<String> validUsername() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(5)
                .ofMaxLength(16)
                .filter(username -> !username.isBlank());
    }

    @Provide
    Arbitrary<String> validJwtToken(@ForAll Integer userId) {
        return Arbitraries.of(jwtUtil.generateToken(userId));
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
