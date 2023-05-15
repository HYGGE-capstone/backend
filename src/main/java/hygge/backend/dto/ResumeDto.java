package hygge.backend.dto;

import hygge.backend.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDto {
    private Long memberId;
    private String content;
    private String title;
    private Long subjectId;

    public ResumeDto(Resume resume) {
        this.memberId = resume.getMember().getId();
        this.content = resume.getContent();
        this.title = resume.getTitle();
        this.subjectId = resume.getSubject().getId();
    }
}
