package br.edu.infnet.pageflow.security.configuration;

import br.edu.infnet.pageflow.security.jwt.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil; // Classe que gera o token

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        String token = jwtUtil.generateToken(username); // Gera o token JWT

        // Criando um cookie seguro com o JWT
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);  // Apenas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // Expira em 1 dia

        // Diferenciar entre ambiente local e produção
        if (request.getServerName().equals("localhost")) {
            cookie.setSecure(false); // Sem HTTPS no local
            cookie.setAttribute("SameSite", "Lax"); // Evita problemas de envio
        } else {
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None"); // Permite requests entre domínios
        }

        response.addCookie(cookie);
    }
}