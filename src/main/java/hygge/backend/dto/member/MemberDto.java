package hygge.backend.dto.member;

import hygge.backend.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @Schema(description = "회원 아이디", defaultValue = "0")
    private Long memberId;

    @Schema(description = "회원 로그인 아이디", defaultValue = "test1")
    private String memberLoginId;

    @Schema(description = "회원 이메일", defaultValue = "song@ajou.ac.kr")
    private String memberEmail;

    @Schema(description = "회원 닉네임", defaultValue = "송명준")
    private String memberNickname;

    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.memberLoginId = member.getLoginId();
        this.memberEmail = member.getEmail();
        this.memberNickname = member.getNickname();
    }
}
