package hygge.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @Schema(description = "액세스 토큰", defaultValue = "accessToken")
    private String accessToken;

    @Schema(description = "리프레쉬 토큰", defaultValue = "refreshToken")
    private String refreshToken;

    @Schema(description = "로그인 아이디", defaultValue = "testid")
    private String loginId;

    @Schema(description = "닉네임", defaultValue = "테스터")
    private String nickname;

    private Long accessTokenExpiresIn;

    private Long refreshTokenExpiresIn;

}
