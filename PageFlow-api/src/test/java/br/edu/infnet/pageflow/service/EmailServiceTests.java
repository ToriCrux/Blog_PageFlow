package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.BlogUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTests {

    @Mock private JavaMailSender mailSender;
    @Mock private MessageSource messageSource;

    @InjectMocks private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(mailSender, messageSource);
    }

    @Test
    void testConstructResetTokenEmail() {
        BlogUser user = new BlogUser();
        user.setEmail("test@example.com");

        String token = "abc123";
        String contextPath = "http://localhost:8080";
        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage(eq("message.resetPasswordEmail"), any(), eq(locale)))
                .thenReturn("Reset your password");

        SimpleMailMessage result = emailService.constructResetTokenEmail(contextPath, locale, token, user);

        assertEquals("noreply@pageflow.com", result.getFrom());
        assertEquals("test@example.com", result.getTo()[0]);
        assertEquals("Reset password", result.getSubject());
        assertTrue(result.getText().contains("Reset your password"));
        assertTrue(result.getText().contains(token));
    }

    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Email body content";

        emailService.sendEmail(to, subject, body);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertEquals("noreply@pageflow.com", sentMessage.getFrom());
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(body, sentMessage.getText());
    }
}
