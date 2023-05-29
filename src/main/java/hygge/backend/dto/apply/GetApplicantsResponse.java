package hygge.backend.dto.apply;

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
public class GetApplicantsResponse {

    @Schema(description = "지원자 목록")
    private List<ApplicantDto> applicants;
}
