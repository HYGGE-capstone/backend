package hygge.backend.dto;

import hygge.backend.entity.Subscribe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeDto {

    @Schema(description = "회원 아이디", defaultValue = "0")
    private Long memberId;

    @Schema(description = "과목 아이디", defaultValue = "0")
    private Long subjectId;

}
