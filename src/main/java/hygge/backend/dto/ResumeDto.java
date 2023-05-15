package hygge.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hygge.backend.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDto {
    @Schema(description = "회원 아이디", defaultValue = "1")
    @JsonIgnore
    private Long memberId;
    @Schema(description = "이력서 내용", defaultValue = "스프링부트 고수이고, 알고리즘 A+ 입니다.")
    private String content;

    @Schema(description = "이력서 제목", defaultValue = "성장하는 백엔드 개발자")
    private String title;

    @Schema(description = "과목 아이디", defaultValue = "100")
    private Long subjectId;

    public ResumeDto(Resume resume) {
        this.memberId = resume.getMember().getId();
        this.content = resume.getContent();
        this.title = resume.getTitle();
        this.subjectId = resume.getSubject().getId();
    }
}
