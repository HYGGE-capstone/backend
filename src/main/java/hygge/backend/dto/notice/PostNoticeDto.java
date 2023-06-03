package hygge.backend.dto.notice;

import hygge.backend.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostNoticeDto {
    private Long teamId;
    private String noticeContent;

    public Notice toEntity() {
        return Notice.builder()
                .content(this.noticeContent)
                .build();
    }
}
