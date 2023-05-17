package hygge.backend.dto.request.teamapplicant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyResultRequestDto {
    @Schema(description = "지원서 아이디", defaultValue = "0")
    private Long teamApplicantId;
}
