package hygge.backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import hygge.backend.entity.Member;
import hygge.backend.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberByRefreshToken(String token) {
        return memberRepository.findByRefreshToken(token)
                .orElseThrow(() -> new RuntimeException("JWT_REFRESH_EXPIRED"));
    }

    @Transactional
    public void setRefreshToken(String loginId, String token) {
        memberRepository.findByLoginId(loginId)
                .ifPresent(member -> member.setRefreshToken(token));
    }

    @Transactional
    public void removeRefreshToken(String token) {
        memberRepository.findByRefreshToken(token)
                .ifPresent(member -> member.setRefreshToken(null));
    }

    public void logout(HttpServletRequest request) {
        try {
            checkHeaderValid(request);
            String refreshToken = request
                    .getHeader(JwtProperties.REFRESH_HEADER_PREFIX)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            removeRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("JWT_REFRESH_NOT_VALID");
        }
    }

    public String createAccessToken(Long id, String loginId) {
        return JWT.create()
                .withSubject(JwtProperties.ACCESS_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim(JwtProperties.ID, id)
                .withClaim(JwtProperties.LOGIN_ID, loginId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createRefreshToken() {
        return JWT.create()
                .withSubject(JwtProperties.REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public void checkHeaderValid(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtProperties.HEADER_PREFIX);
        String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_PREFIX);

        if (accessToken == null) {
            throw new RuntimeException("JWT_ACCESS_NOT_VALID");
        } else if (refreshToken == null) {
            throw new RuntimeException("JWT_REFRESH_NOT_VALID");
        }
    }

    public void checkTokenValid(String token) {
        JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token);
    }

    public boolean isExpiredToken(String token) {
        try {
            checkTokenValid(token);
        } catch (RuntimeException e) {
            log.info("만료 토큰");
            return true;
        }
        return false;
    }

    public boolean isNeedToUpdateRefreshToken(String token) {
        try {
            Date expiresAt = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token)
                    .getExpiresAt();

            Date current = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 7);

            Date after7dayFromToday = calendar.getTime();

            // 7일 이내에 만료
            if (expiresAt.before(after7dayFromToday)) {
                log.info("리프레쉬 토큰 7일 이내 만료");
                return true;
            }
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }
}
