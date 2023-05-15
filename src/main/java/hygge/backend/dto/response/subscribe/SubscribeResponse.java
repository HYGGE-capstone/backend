package hygge.backend.dto.response.subscribe;

import hygge.backend.dto.SubjectDto;
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
public class SubscribeResponse {

    @Schema(description = "구독한 과목 정보")
    private List<SubjectDto> subscribes;
}
