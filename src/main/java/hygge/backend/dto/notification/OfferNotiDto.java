package hygge.backend.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferNotiDto {
    private String subjectName;
    private String subjectCode;
    private String teamName;
    private Long to;

    private String from; // <<subjectName>>(<<subjectCode>>)
    private String msg;  // 팀 <<teamName>> 에서 합류 제안을 보냈습니다.

    public OfferNotiDto(String subjectName, String subjectCode, String teamName, Long to) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.teamName = teamName;
        this.to = to;

        this.from = subjectName + "(" + subjectCode + ")";
        this.msg = "팀 \"" + teamName + "\" 에서 합류 제안을 보냈습니다.";
    }
}
