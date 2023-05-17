package hygge.backend.dto.response.teamapplicant;

import hygge.backend.dto.ApplicantDto;
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
    private List<ApplicantDto> applicants;
}
