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
    private Long applicantId;
    @Schema(description = "지원자 로그인 아이디", defaultValue = "testId")
    private String applicantLoginId;
    @Schema(description = "지원자 닉네임", defaultValue = "TESTER")
    private String applicantNickname;
    @Schema(description = "지원서 아이디", defaultValue = "0")
    private Long teamApplicantId;


    public ApplicantDto(Member applicant, Long teamApplicantId) {
        this.applicantId = applicant.getId();
        this.applicantLoginId = applicant.getLoginId();
        this.applicantNickname = applicant.getNickname();
        this.teamApplicantId = teamApplicantId;
    }
}
