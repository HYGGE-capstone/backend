package hygge.backend.dto.response.team;

import hygge.backend.dto.TeamDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private List<TeamDto> teams;
}
