package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewTeamMemberNotiDto {
    private Long teamId;
    private String teamName;
    private String memberNickname;
    private String memberLoginId;

}
