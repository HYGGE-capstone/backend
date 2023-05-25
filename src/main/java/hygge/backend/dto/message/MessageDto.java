package hygge.backend.dto.message;

import hygge.backend.entity.Message;
import hygge.backend.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long fromId;
    private Long toId;
    private String content;
    private String createdTime;
    private boolean isOpened;

    public MessageDto(Message message) {
        this.fromId = message.getFrom().getId();
        this.toId = message.getTo().getId();
        this.content = message.getContent();
        this.createdTime = message.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.isOpened = message.isOpened();
    }
}
