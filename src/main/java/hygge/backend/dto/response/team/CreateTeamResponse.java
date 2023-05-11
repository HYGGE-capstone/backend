package hygge.backend.dto.response.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamResponse {
    private Long teamId;
    private Long leaderId;
    private String name;
    private Long subjectId;
    private String title;
    private String description;
    private int maxMember;
}
