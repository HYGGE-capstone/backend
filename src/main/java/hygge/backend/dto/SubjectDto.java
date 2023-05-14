package hygge.backend.dto;

import hygge.backend.entity.Semester;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {

    @Schema(description = "과목 아이디", defaultValue = "0")
    private Long subjectId;

    @Schema(description = "과목 이름", defaultValue = "SW캡스톤디자인")
    private String name;

    @Schema(description = "과목 코드", defaultValue = "F100")
    private String code;

    @Schema(description = "과목 연도", defaultValue = "2023")
    private int year;

    @Schema(description = "과목 학기", defaultValue = "SPRING")
    private Semester semester;

    @Schema(description = "과목 교수명", defaultValue = "윤대균")
    private String pName;

    @Schema(description = "과목 시간", defaultValue = "월F(팔309) 월G(팔317) 월H(팔317) 목F(팔309) 목G(팔336) 목H(팔336)")
    private String time;
}
