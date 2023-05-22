package hygge.backend.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReissueDto {
    @Schema(description = "액세스 토큰", defaultValue = "ACCESS_TOKEN")
    private String accessToken;

    @Schema(description = "리프레쉬 토큰", defaultValue = "REFRESH_TOKEN")
    private String refreshToken;
}
