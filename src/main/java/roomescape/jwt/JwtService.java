package roomescape.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Autowired
    private JwtUtil jwtUtil;

    public Long getUserIdFromToken(String token) {
        Claims claims = jwtUtil.getClaimsFromToken(token);
        try {
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new IllegalArgumentException("Claims에서 User ID를 추출할 수 없습니다.", e);
        }
    }
}

