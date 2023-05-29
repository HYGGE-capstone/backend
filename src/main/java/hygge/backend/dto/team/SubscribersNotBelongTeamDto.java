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
public class SubscribersNotBelongTeamDto {
    private Long memberId;
    private String loginId;
    private String nickname;

    public SubscribersNotBelongTeamDto(Member member) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
        this.nickname = member.getNickname();
    }
}
