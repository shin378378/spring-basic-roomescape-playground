package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.jwt.JwtService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private JwtService jwtService;
    private MemberDao memberDao;
    private LoginService loginService;

    public LoginController(JwtService jwtService, MemberDao memberDao, LoginService loginService) {
        this.jwtService = jwtService;
        this.memberDao = memberDao;
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            String token = loginService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            Cookie cookie = loginService.createAuthCookie(token);
            response.addCookie(cookie);
            Map<String, Object> responseBody = loginService.createLoginResponse(token);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkLogin(@CookieValue(name = "token", required = true) String token) {
        try {
            Long userId = jwtService.getUserIdFromToken(token);
            Member member = memberDao.findById(userId);

            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Map<String, String> response = new HashMap<>();
            response.put("name", member.getName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
