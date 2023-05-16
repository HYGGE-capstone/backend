package hygge.backend.dto.response.subject;

import hygge.backend.dto.SubjectDto;
import hygge.backend.entity.Subject;
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
public class SearchSubjectResponse {

    @Schema(description = "조회한 과목 정보")
    private List<SubjectDto> subjects;
}
