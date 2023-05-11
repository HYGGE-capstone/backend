package hygge.backend.dto.response.subject;

import hygge.backend.entity.Subject;
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
    private List<Subject> subjects;
}
