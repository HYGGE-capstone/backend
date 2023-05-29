package hygge.backend.dto.message;

import hygge.backend.entity.MessageRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRoomDto {
    private Long messageRoomId;
    private Long toId;
    private String toLoginId;
    private String toNickname;

    private boolean isDirty;
    private LocalDateTime lastUpdateTime;

    public MessageRoomDto(MessageRoom messageRoom) {
        this.messageRoomId = messageRoom.getId();
        this.toId = messageRoom.getTo().getId();
        this.toLoginId = messageRoom.getTo().getLoginId();
        this.toNickname = messageRoom.getTo().getNickname();
        this.lastUpdateTime = messageRoom.getLastUpdateTime();
        this.isDirty = messageRoom.isDirty();
    }
}
