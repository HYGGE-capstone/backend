package hygge.backend.dto.subscribe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeRequest {

    @Schema(description = "과목 아이디", defaultValue = "0")
    private Long subjectId;

}
