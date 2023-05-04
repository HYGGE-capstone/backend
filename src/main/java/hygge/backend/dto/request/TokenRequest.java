package hygge.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequest {
    @Schema(description = "액세스 토큰", defaultValue = "accessToken")
    private String accessToken;

    @Schema(description = "리프레쉬 토큰", defaultValue = "refreshToken")
    private String refreshToken;
}
