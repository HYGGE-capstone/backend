package hygge.backend.dto.apply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyResultDto {
    @Schema(description = "팀 아이디", defaultValue = "0")
    private Long teamId;
    @Schema(description = "지원자 아이디", defaultValue = "0")
    private Long applicantId;
    @Schema(description = "결과", defaultValue = "ACCEPT")
    private String result;
}
