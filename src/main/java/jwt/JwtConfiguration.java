package jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
