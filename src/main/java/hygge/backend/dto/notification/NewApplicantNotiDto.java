package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewApplicantNotiDto { // 새로운 지원자 알림 (from 팀 이름, content 지원자 로그인 아이디, 지원자 닉네임)
    private String teamName;
    private String applicantLoginId;
    private String applicantNickname;
    private Long leaderId;
}
