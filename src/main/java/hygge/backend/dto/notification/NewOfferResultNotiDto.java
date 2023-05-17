package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewOfferResultNotiDto {
    private String teamName;
    private String applicantLoginId;
    private String applicantNickname;
    private boolean accept;
    private Long leaderId;
}
