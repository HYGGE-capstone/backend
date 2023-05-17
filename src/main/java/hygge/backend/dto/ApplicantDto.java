package hygge.backend.dto;

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
public class ApplicantDto {
    @Schema(description = "지원자 아이디", defaultValue = "0")
    private Long id;
    @Schema(description = "지원자 로그인 아이디", defaultValue = "testId")
    private String loginId;
    @Schema(description = "지원자 닉네임", defaultValue = "TESTER")
    private String nickname;

    public ApplicantDto(Member applicant) {
        this.id = applicant.getId();
        this.loginId = applicant.getLoginId();
        this.nickname = applicant.getNickname();
    }
}
