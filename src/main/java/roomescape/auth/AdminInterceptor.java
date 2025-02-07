package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Role;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;

    public AdminInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = extractTokenFromCookies(request.getCookies());
        if (token == null) {
            response.sendRedirect("/login");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다.");
        }

        Claims claims = jwtUtils.getClaimsFromToken(token);
        String role = claims.get("role", String.class);

        if (!Role.ADMIN.name().equals(role)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }
        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
