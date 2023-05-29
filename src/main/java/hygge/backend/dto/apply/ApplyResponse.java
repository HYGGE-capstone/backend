package hygge.backend.dto.apply;

import hygge.backend.entity.TeamApplicant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyResponse {
    @Schema(description = "지원서 아이디", defaultValue = "0")
    private Long teamApplicantId;
    @Schema(description = "지원자 아이디", defaultValue = "0")
    private Long memberId;
    @Schema(description = "팀 아이디", defaultValue = "0")
    private Long teamId;

    public ApplyResponse(TeamApplicant teamApplicant) {
        this.teamApplicantId = teamApplicant.getId();
        this.memberId = teamApplicant.getApplicant().getId();
        this.teamId = teamApplicant.getTeam().getId();
    }
}
