package hygge.backend.dto.team;

import hygge.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembersByTeamDto {
    private Long memberId;
    private String loginId;
    private String nickname;
    private boolean isLeader;

    public MembersByTeamDto(Member member, Long leaderId) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
        this.nickname = member.getNickname();

        if (member.getId() == leaderId) {
            this.isLeader = true;
        }else {
            this.isLeader = false;
        }
    }
}
