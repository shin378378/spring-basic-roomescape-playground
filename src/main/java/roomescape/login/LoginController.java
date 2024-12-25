package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MemberDao memberDao;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        try {
            Member member = memberDao.findByEmailAndPassword(email, password);

            if (member != null) {
                String token = jwtUtil.generateToken(member.getId());

                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);

                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                response.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
                response.setHeader("Keep-Alive", "timeout=60");

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("token", token);
                responseBody.put("message", "Login successful");

                return ResponseEntity.ok(responseBody);
            }
        } catch (EmptyResultDataAccessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Invalid email or password");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkLogin(@CookieValue(name = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
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
