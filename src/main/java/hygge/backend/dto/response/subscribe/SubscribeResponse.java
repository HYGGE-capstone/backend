package hygge.backend.dto.response.subscribe;

import hygge.backend.dto.SubjectDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResponse {
    private List<SubjectDto> subscribes;
}
