package hygge.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
    private Long teamId;
    private String teamName;
    private String teamTitle;
    private String teamDescription;
    private int maxMember;

    private Long subjectId;
    private String subjectName;
    private String subjectCode;
}
