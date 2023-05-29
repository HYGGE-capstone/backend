package hygge.backend.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class LoginRequest {
    @Schema(description = "로그인 아이디", defaultValue = "testId")
    private String loginId;

    @Schema(description = "비밀번호", defaultValue = "12345678")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginId, password);
    }
}
