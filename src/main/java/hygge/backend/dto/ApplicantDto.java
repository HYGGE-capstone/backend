package hygge.backend.dto;

import hygge.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDto {
    private Long id;
    private String loginId;
    private String nickname;

    public ApplicantDto(Member applicant) {
        this.id = applicant.getId();
        this.loginId = applicant.getLoginId();
        this.nickname = applicant.getNickname();
    }
}
