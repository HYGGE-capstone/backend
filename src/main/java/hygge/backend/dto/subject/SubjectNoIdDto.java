package hygge.backend.dto.subject;

import hygge.backend.entity.School;
import hygge.backend.entity.Semester;
import hygge.backend.entity.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectNoIdDto {

    @Schema(description = "학교 아이디", defaultValue = "0")
    private Long schoolId;

    @Schema(description = "과목 이름", defaultValue = "SW캡스톤디자인")
    private String name;

    @Schema(description = "과목 코드", defaultValue = "F100")
    private String code;

    @Schema(description = "과목 연도", defaultValue = "2023")
    private int year;

    @Schema(description = "과목 학기", defaultValue = "SPRING")
    private Semester semester;

    @Schema(description = "과목 교수명", defaultValue = "윤대균")
    private String professorName;

    @Schema(description = "과목 시간", defaultValue = "월F(팔309) 월G(팔317) 월H(팔317) 목F(팔309) 목G(팔336) 목H(팔336)")
    private String time;

    public SubjectNoIdDto(Subject subject) {
        this.name = subject.getName();
        this.code = subject.getCode();
        this.year = subject.getYear();
        this.semester = subject.getSemester();
        this.professorName = subject.getProfessorName();
        this.time = subject.getTime();
        this.schoolId = subject.getSchool().getId();
    }

    public Subject toEntity(School school) {

        return Subject.builder()
                .name(this.name)
                .code(this.code)
                .year(this.year)
                .semester(this.semester)
                .professorName(this.professorName)
                .time(this.time)
                .school(school)
                .build();

    }
}
