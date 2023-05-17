package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewApplyResultNotiDto {
    private Long applicantId;
    private String teamName;
    private boolean isAccept;
}
