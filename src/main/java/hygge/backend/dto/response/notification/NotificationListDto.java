package hygge.backend.dto.response.notification;

import hygge.backend.dto.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationListDto {
    private List<NotificationDto> notifications;
}
