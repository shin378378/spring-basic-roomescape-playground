package roomescape.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class JwtService {
    private final JwtUtil jwtUtil;
    private final MemberDao memberDao;

    public JwtService(JwtUtil jwtUtil, MemberDao memberDao) {
        this.jwtUtil = jwtUtil;
        this.memberDao = memberDao;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = jwtUtil.getClaimsFromToken(token);
        try {
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new IllegalArgumentException("Claims에서 User ID를 추출할 수 없습니다.", e);
        }
    }

    public Member getMemberFromToken(String token) {
        Long userId = getUserIdFromToken(token);
        Member member = memberDao.findById(userId);
        if (member == null) {
            throw new IllegalArgumentException("토큰으로부터 유저를 찾을 수 없습니다.");
        }
        return member;
    }
}
