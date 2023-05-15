package hygge.backend.dto.request.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequest {
    @Schema(description = "팀 이름", defaultValue = "HYGGE")
    private String name;

    @Schema(description = "과목 아이디", defaultValue = "100")
    private Long subjectId;

    @Schema(description = "팀 주제", defaultValue = "팀원 매칭 플랫폼")
    private String title;

    @Schema(description = "팀 소개", defaultValue = "팀원 매칭 플랫폼을 개발할 예정입니다.")
    private String description;

    @Schema(description = "최대 멤버 수", defaultValue = "4")
    private int maxMember;
}
