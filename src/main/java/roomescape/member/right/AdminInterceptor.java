package roomescape.member.right;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.jwt.JwtService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.Arrays;

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
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다.");
        }

        Long userId = jwtService.getUserIdFromToken(token);
        Member member = memberDao.findById(userId);

        if (member == null || !"ADMIN".equals(member.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다.");
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
