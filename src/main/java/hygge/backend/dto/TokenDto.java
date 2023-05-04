package hygge.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {


    @JsonIgnore
    private String grantType;

    @Schema(description = "액세스 토큰", defaultValue = "accessToken")
    private String accessToken;

    @Schema(description = "리프레쉬 토큰", defaultValue = "refreshToken")
    private String refreshToken;

    @JsonIgnore
    private Long accessTokenExpiresIn;

    @JsonIgnore
    private Long refreshTokenExpiresIn;

    @JsonIgnore
    private String authority;

    @JsonIgnore
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }
}
