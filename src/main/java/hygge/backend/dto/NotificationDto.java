package hygge.backend.dto;

import hygge.backend.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String from;
    private String content;
    private LocalDateTime createdTime;

    public NotificationDto(Notification notification) {
        this.from = notification.getFrom();
        this.content = notification.getContent();
        this.createdTime = notification.getCreatedTime();
    }
}
