package jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.member.MemberRepository;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {
    private final JwtProperties jwtProperties;

    public JwtConfiguration(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(jwtProperties);
    }

    @Bean
    public JwtService jwtService(JwtUtils jwtUtils, MemberRepository memberRepository) {
        return new JwtService(jwtUtils, memberRepository);
    }
}
