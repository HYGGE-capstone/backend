package hygge.backend.dto.response.offer;

import hygge.backend.entity.Offer;
import hygge.backend.entity.TeamApplicant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponse {
    @Schema(description = "합류 제안 아이디", defaultValue = "0")
    private Long offerId;
    @Schema(description = "구독자 아이디", defaultValue = "0")
    private Long memberId;
    @Schema(description = "팀 아이디", defaultValue = "0")
    private Long teamId;

    public OfferResponse(Offer offer) {
        this.offerId = offer.getId();
        this.memberId = offer.getMember().getId();
        this.teamId = offer.getTeam().getId();
    }
}
