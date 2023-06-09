package hygge.backend.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamDto {
    @Schema(description = "팀 아이디", defaultValue = "11")
    private Long teamId;

    @Schema(description = "팀 이름", defaultValue = "HYGGE")
    private String teamName;

    @Schema(description = "팀 주제", defaultValue = "팀원 매칭 플랫폼")
    private String teamTitle;

    @Schema(description = "팀 소개", defaultValue = "팀원 매칭 플랫폼을 개발할 계획입니다.")
    private String teamDescription;
}
