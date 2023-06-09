package hygge.backend.dto.team;

import hygge.backend.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {

    @Schema(description = "팀 아이디", defaultValue = "11")
    private Long teamId;

    @Schema(description = "팀 이름", defaultValue = "HYGGE")
    private String teamName;

    @Schema(description = "팀 주제", defaultValue = "팀원 매칭 플랫폼")
    private String teamTitle;

    @Schema(description = "팀 소개", defaultValue = "팀원 매칭 플랫폼을 개발할 계획입니다.")
    private String teamDescription;

    @Schema(description = "최대 멤버 수 ", defaultValue = "5")
    private int maxMember;

    @Schema(description = "현재 멤버 수 ", defaultValue = "1")
    private int numMember;


    @Schema(description = "과목 아이디", defaultValue = "100")
    private Long subjectId;

    @Schema(description = "과목 이름", defaultValue = "SW캡스톤디자인")
    private String subjectName;

    @Schema(description = "과목 코드", defaultValue = "F100")
    private String subjectCode;

    @Schema(description = "리더 아이디", defaultValue = "0")
    private Long leaderId;

    @Schema(description = "리더 여부", defaultValue = "false")
    private boolean isLeader;

    public TeamDto(Team team, boolean isLeader) {
        this.teamId = team.getId();
        this.teamName = team.getName();
        this.teamTitle = team.getTitle();
        this.teamDescription = team.getDescription();
        this.maxMember = team.getMaxMember();
        this.numMember = team.getNumMember();
        this.leaderId = team.getLeader().getId();
        this.isLeader = isLeader;
        this.subjectCode = team.getSubject().getCode();
        this.subjectId = team.getSubject().getId();
        this.subjectName = team.getSubject().getName();
    }
}
