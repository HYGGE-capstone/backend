package hygge.backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.backend.dto.request.LoginRequest;
import hygge.backend.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        super.setFilterProcessesUrl(filterProcessesUrl);
    }

    // login 요청 시 로그인을 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("TRY LOGIN LOGIN_ID & PASSWORD : 인증 검증 _ JwtAuthenticationFilter.attemptAuthentication");
        ObjectMapper om = new ObjectMapper();
        try {
            LoginRequest login = om.readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(login.getLoginId(), login.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 인증이 정상적으로 완료되면 실행된다.
    // JWT를 만들어서 REQUEST 한 사용자에게 토큰을 RESPONSE 해준다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException{
        log.info("인증 완료 : JwtAuthenticationFilter.successfulAuthentication");

        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        Member member = principal.getMember();
        String accessToken = jwtService.createAccessToken(member.getId(), member.getLoginId());
        String refreshToken = jwtService.createRefreshToken();

        // login 성공 -> Refresh 토큰 재발급
        jwtService.setRefreshToken(member.getLoginId(), refreshToken);

        response.addHeader(JwtProperties.HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + refreshToken);
        setSuccessResponse(response, "로그인 성공");
        log.info("=============================인증 프로세스 완료=============================");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("인증 실패 : JwtAuthenticationFilter.unsuccessfulAuthentication");
        String failReason = failed.getMessage();
        setFailResponse(response, failReason);
        log.info("=============================인증 프로세스 완료=============================");
    }

    private void setSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("code", 1);
        jsonObject.put("message", message);

        response.getWriter().print(jsonObject);
    }

    private void setFailResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        jsonObject.put("code", -1);
        jsonObject.put("message", message);

        response.getWriter().print(jsonObject);
    }
}
