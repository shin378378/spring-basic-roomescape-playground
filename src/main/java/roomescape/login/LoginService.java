package roomescape.login;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.jwt.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private JwtUtil jwtUtil;
    private MemberDao memberDao;

    public LoginService(JwtUtil jwtUtil, MemberDao memberDao) {
        this.jwtUtil = jwtUtil;
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
}
