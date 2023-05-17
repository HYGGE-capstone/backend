package hygge.backend.dto.response.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetSubscribersNotBelongTeamResponse {
    private List<SubscribersNotBelongTeamDto> members;
}
