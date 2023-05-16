package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewTeamNotiDto {
    private Long subjectId;
    private String teamName;
    private String subjectName;
    private String subjectCode;
}
