package hygge.backend.dto.response.offer;

import hygge.backend.dto.OfferTeamDto;
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
public class GetOffersResponse {

    @Schema(description = "제안 팀 목록")
    private List<OfferTeamDto> offerTeams;
}
