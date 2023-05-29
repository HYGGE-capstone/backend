package hygge.backend.dto.offer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferResultRequestDto {
    @Schema(description = "팀 합류 제안 아이디", defaultValue = "0")
    private Long offerId;
}
