package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.*;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.EmailService;
import br.edu.infnet.pageflow.service.JwtTokenService;
import br.edu.infnet.pageflow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtUtil jwtUtil;
    private final MessageSource messages;
    private final JavaMailSender mailSender;
    private final JwtTokenService jwtTokenService;

    // TODO - Long parameter list, fix it
    public AuthController(UserService userService, EmailService emailService, AuthenticationManager authenticationManager, AuthUserDetailsService authUserDetailsService, JwtUtil jwtUtil, MessageSource messages, JavaMailSender mailSender, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.authUserDetailsService = authUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.messages = messages;
        this.mailSender = mailSender;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody @Valid SignupRequest signupRequest) {
        BlogUser createdUser = userService.createUser(signupRequest);

        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (DisabledException disabledException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not activated");
        }

        final UserDetails userDetails = authUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestBody ResetPasswordRequest resetPasswordRequest) {

        BlogUser user = userService.getUserByEmail(resetPasswordRequest.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(messages.getMessage("message.badEmail", null, request.getLocale()));
        }

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        mailSender.send(emailService.constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));

        return ResponseEntity.ok(
                messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));

    }

    @PostMapping("/changePassword/{resetToken}")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @PathVariable String resetToken) {
        return validateAndRespondToResetToken(request, resetToken);
    }

    @PostMapping("/saveNewPassword")
    public ResponseEntity<?> saveNewPassword(HttpServletRequest request, @Valid @RequestBody NewPasswordRequest passwordRequest) {

        ResponseEntity<?> validationResponse = validateAndRespondToResetToken(request, passwordRequest.getToken());

        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }

        BlogUser user = userService.getUserByPasswordResetToken(passwordRequest.getToken());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    messages.getMessage("message.userNotFound", null, request.getLocale())
            );

        }

        userService.changeUserPassword(user, passwordRequest.getNewPassword());
        jwtTokenService.deleteToken(passwordRequest.getToken());

        return ResponseEntity.ok(messages.getMessage("message.updatePasswordSuc", null, request.getLocale()));
    }

    private ResponseEntity<?> validateAndRespondToResetToken(HttpServletRequest request, String token) {

        String validationResult = jwtTokenService.validatePasswordResetToken(token);
        String message = messages.getMessage("token.message." + validationResult, null, request.getLocale());

        if (Objects.equals(validationResult, "valid")) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    private String getAppUrl(HttpServletRequest request) {
//        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
    }
}
