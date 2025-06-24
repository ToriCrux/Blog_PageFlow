package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.PasswordResetToken;
import br.edu.infnet.pageflow.repository.PasswordTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenServiceTests {

    @Mock private PasswordTokenRepository passwordTokenRepository;

    @InjectMocks private JwtTokenService jwtTokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        jwtTokenService = new JwtTokenService(passwordTokenRepository);
    }

    @Test
    void testValidatePasswordResetToken_NotFound() {
        when(passwordTokenRepository.findByToken("invalid")).thenReturn(null);

        String result = jwtTokenService.validatePasswordResetToken("invalid");

        assertEquals("invalidToken", result);
    }

    @Test
    void testValidatePasswordResetToken_Expired() {
        PasswordResetToken expiredToken = new PasswordResetToken();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -10); // já expirado
        expiredToken.setExpiryDate(cal.getTime());

        when(passwordTokenRepository.findByToken("expired")).thenReturn(expiredToken);

        String result = jwtTokenService.validatePasswordResetToken("expired");

        assertEquals("expired", result);
    }

    @Test
    void testValidatePasswordResetToken_Valid() {
        PasswordResetToken validToken = new PasswordResetToken();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10); // ainda válido
        validToken.setExpiryDate(cal.getTime());

        when(passwordTokenRepository.findByToken("valid")).thenReturn(validToken);

        String result = jwtTokenService.validatePasswordResetToken("valid");

        assertEquals("valid", result);
    }

    @Test
    void testDeleteToken() {
        PasswordResetToken token = new PasswordResetToken();
        when(passwordTokenRepository.findByToken("abc")).thenReturn(token);

        jwtTokenService.deleteToken("abc");

        verify(passwordTokenRepository).delete(token);
    }
}
