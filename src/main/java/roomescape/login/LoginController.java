package roomescape.login;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

        // 데이터베이스에서 사용자 조회
        Member member = memberDao.findByEmailAndPassword(email, password);

        if (member != null) {
            // JWT 토큰 생성
            String token = jwtUtil.generateToken(member.getId());

            // Cookie에 토큰 추가
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            System.out.println(member.getName());

            // 헤더 설정
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
            response.setHeader("Keep-Alive", "timeout=60");

            // 응답 본문에 토큰 추가
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("message", "Login successful");

            return ResponseEntity.ok(responseBody);
        }
        // 인증 실패 메시지 포함
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
            // JWT 토큰에서 사용자 ID 추출
            Long userId = jwtUtil.getUserIdFromToken(token);

            // 데이터베이스에서 사용자 조회
            Member member = memberDao.findByName(userId.toString());

            // 응답 데이터 생성
            Map<String, String> response = new HashMap<>();
            response.put("name", member.getName());
            response.put("role", member.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
