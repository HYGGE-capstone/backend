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
public class EmailAuthResponse {
    @Schema(description = "학교 아이디", defaultValue = "0")
    private Long schoolId;

    @Schema(description = "학교 이름", defaultValue = "아주대학교")
    private String schoolName;

    @Schema(description = "인증 코드", defaultValue = "xxxxxxxx")
    private String code;
}
