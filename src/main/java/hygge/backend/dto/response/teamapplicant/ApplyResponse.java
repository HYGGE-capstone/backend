package hygge.backend.dto.response.teamapplicant;

import hygge.backend.entity.TeamApplicant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyResponse {
    private Long teamApplicantId;
    private Long memberId;
    private Long teamId;

    public ApplyResponse(TeamApplicant teamApplicant) {
        this.teamApplicantId = teamApplicant.getId();
        this.memberId = teamApplicant.getApplicant().getId();
        this.teamId = teamApplicant.getTeam().getId();
    }
}
