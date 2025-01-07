package roomescape.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private JwtUtil jwtUtil;

    public JwtService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = jwtUtil.getClaimsFromToken(token);
        try {
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new IllegalArgumentException("Claims에서 User ID를 추출할 수 없습니다.", e);
        }
    }
}

