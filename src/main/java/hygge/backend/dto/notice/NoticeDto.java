package hygge.backend.dto.notice;

import hygge.backend.entity.Notice;
import hygge.backend.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {
    private Long noticeId;
    private String noticeContent;
    private Long teamId;
    private String teamName;

    public NoticeDto(Notice notice) {
        this.noticeId = notice.getId();
        this.noticeContent = notice.getContent();
        this.teamId = notice.getTeam().getId();
        this.teamName = notice.getTeam().getName();
    }

    public NoticeDto(Notice notice, Team team) {
        this.noticeId = notice.getId();
        this.noticeContent = notice.getContent();
        this.teamId = team.getId();
        this.teamName = team.getName();
    }
}
