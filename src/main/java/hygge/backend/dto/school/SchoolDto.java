package hygge.backend.dto.school;

import hygge.backend.entity.School;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDto {
    @Schema(description = "학교 아이디", defaultValue = "0")
    private Long schoolId;

    @Schema(description = "학교 이름", defaultValue = "아주대학교")
    private String schoolName;

    @Schema(description = "학교 이메일 형식", defaultValue = "ajou.ac.kr")
    private String schoolEmailForm;

    public SchoolDto(School school) {
        this.schoolId = school.getId();
        this.schoolName = school.getSchoolName();
        this.schoolEmailForm = school.getEmailForm();
    }
}
