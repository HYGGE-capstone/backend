package hygge.backend.dto.subject;

import hygge.backend.dto.subject.SubjectDto;
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
