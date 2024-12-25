package roomescape.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private JwtUtil jwtUtil;

    public AdminInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            response.setStatus(401);
            return false;
        }

        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            Member member = memberDao.findById(userId);

            if (member == null || !"ADMIN".equals(member.getRole())) {
                response.setStatus(401);
                return false;
            }
        } catch (Exception e) {
            response.setStatus(401); // 예외 발생 시 Unauthorized
            return false;
        }

        return true;
    }


}
