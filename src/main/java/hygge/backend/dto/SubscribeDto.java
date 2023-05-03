package hygge.backend.dto;

import hygge.backend.entity.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeDto {
    private Long memberId;
    private Long subjectId;

}
