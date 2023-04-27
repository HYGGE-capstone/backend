package hygge.backend.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import hygge.backend.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 거친다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("CHECK JWT : 인가(권한) 검증 _ JwtAuthorizationFilter.doFilterInternal");

        try {
            jwtService.checkHeaderValid(request);
            String accessToken = request
                    .getHeader(JwtProperties.HEADER_PREFIX)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            String refreshToken = request
                    .getHeader(JwtProperties.REFRESH_HEADER_PREFIX)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            jwtService.checkTokenValid(refreshToken);

            log.info("리프레쉬 토큰 회원 조회");
            Member memberByRefreshToken = jwtService.getMemberByRefreshToken(refreshToken);
            String loginId = memberByRefreshToken.getLoginId();
            Long id = memberByRefreshToken.getId();

            // 리프레쉬 토큰이 7일 이내 만료일 경우 리프레쉬 토큰도 재발급
            if (jwtService.isNeedToUpdateRefreshToken(refreshToken)) {
                refreshToken = jwtService.createRefreshToken();
                response.addHeader(JwtProperties.REFRESH_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + refreshToken);
                jwtService.setRefreshToken(loginId, refreshToken);
            }

            try {
                log.info("액세스 토큰 검증");
                jwtService.checkTokenValid(accessToken);
            } catch (TokenExpiredException expiredException) {
                log.error("ACCESS TOKEN REISUUE : JWT_ACCESS_EXPIRED");
                accessToken = jwtService.createAccessToken(id, loginId);
                response.addHeader(JwtProperties.HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + accessToken);
            }

            PrincipalDetails principalDetails = new PrincipalDetails(memberByRefreshToken);
            Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            request.setAttribute("JWT EXCEPTION", "JWT_EXCEPTION");
        }
        chain.doFilter(request, response);
    }
}
