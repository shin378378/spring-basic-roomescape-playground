package roomescape.login;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.jwt.JwtService;
import roomescape.jwt.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private JwtUtil jwtUtil;
    private JwtService jwtService;
    private MemberDao memberDao;

    public LoginService(JwtUtil jwtUtil, JwtService jwtService, MemberDao memberDao) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.memberDao = memberDao;
    }

    public String authenticate(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String role = member.getRole();
        return jwtUtil.generateToken(member.getId(), role);
    }

    public Cookie createAuthCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Map<String, Object> createLoginResponse(String token) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("message", "Login successful");
        return responseBody;
    }

    public Member validateUserFromToken(String token) {
        Long userId = jwtService.getUserIdFromToken(token);
        Member member = memberDao.findById(userId);

        if (member == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        return member;
    }
}
