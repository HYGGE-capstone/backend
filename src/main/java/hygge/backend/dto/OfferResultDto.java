package hygge.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferResultDto {
    @Schema(description = "팀 아이디", defaultValue = "0")
    private Long teamId;
    @Schema(description = "구독자 아이디", defaultValue = "0")
    private Long subscriberId;
    @Schema(description = "결과", defaultValue = "ACCEPT")
    private String result;
}
