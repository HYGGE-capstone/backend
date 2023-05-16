package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewSubscriberNotiDto {
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String subscriberNickname;
    private String subscriberLoginId;
}
