package br.edu.infnet.pageflow.controller;

import br.edu.infnet.pageflow.dto.LoginRequest;
import br.edu.infnet.pageflow.dto.LoginResponse;
import br.edu.infnet.pageflow.dto.SignupRequest;
import br.edu.infnet.pageflow.entities.BlogUser;
import br.edu.infnet.pageflow.security.jwt.JwtUtil;
import br.edu.infnet.pageflow.service.AuthUserDetailsService;
import br.edu.infnet.pageflow.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, AuthUserDetailsService authUserDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authUserDetailsService = authUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/hello")
    public String greetings() {
        return "Hello from unsecured content";
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
    public LoginResponse loginUser(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not activated");
            return null;
        }

        final UserDetails userDetails = authUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new LoginResponse(jwt);
    }
}
