package jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secret;
    private final long EXPIRATION_TIME_MILLIS= 1000 * 60 * 60 * 24;

    public JwtProperties(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public long getEXPIRATION_TIME_MILLIS() {
        return EXPIRATION_TIME_MILLIS;
    }
}

