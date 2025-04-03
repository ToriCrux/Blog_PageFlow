package br.edu.infnet.pageflow.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final int COOKIE_EXPIRATION = 60 * 60 * 24; // 1 dia

    public static void addJwtCookie(String jwt, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_EXPIRATION);

        if ("localhost".equals(request.getServerName())) {
            cookie.setSecure(false);
            cookie.setAttribute("SameSite", "Lax");
        } else {
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "None");
        }

        response.addCookie(cookie);
    }
}
