package hygge.backend.dto.request.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequest {
    private String name;
    private Long subjectId;
    private String title;
    private String description;
    private int maxMember;
}
