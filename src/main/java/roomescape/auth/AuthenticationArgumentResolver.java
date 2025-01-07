package roomescape.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.jwt.JwtService;
import roomescape.member.Member;

import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authentication.class)
                && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        String token = extractTokenFromCookies(webRequest)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다. 로그인이 필요합니다."));

        return jwtService.getMemberFromToken(token);
    }

    private Optional<String> extractTokenFromCookies(NativeWebRequest webRequest) {
        String cookieHeader = webRequest.getHeader("Cookie");
        if (cookieHeader == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookieHeader.split(";"))
                .map(String::trim)
                .filter(cookie -> cookie.startsWith("token="))
                .map(cookie -> cookie.substring("token=".length()))
                .findFirst();
    }
}
