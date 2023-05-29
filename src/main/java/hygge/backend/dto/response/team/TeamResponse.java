package hygge.backend.dto.response.team;

import hygge.backend.dto.team.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "팀 정보")
    private List<TeamDto> teams;
}
