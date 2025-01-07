package roomescape.member.right;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.jwt.JwtService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private MemberDao memberDao;
    private JwtService jwtService;

    public AdminInterceptor(MemberDao memberDao, JwtService jwtService) {
        this.memberDao = memberDao;
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;

        Cookie[] cookies = request.getCookies();
        token = extractTokenFromCookies(cookies);

        if (token == null) {
            response.setStatus(401);
            return false;
        }

        Long userId = jwtService.getUserIdFromToken(token);
        Member member = memberDao.findById(userId);

        if (member == null || !"ADMIN".equals(member.getRole())) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
