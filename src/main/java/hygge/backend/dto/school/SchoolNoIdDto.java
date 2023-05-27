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
public class SchoolNoIdDto {

    @Schema(description = "학교 이름", defaultValue = "아주대학교")
    private String schoolName;

    @Schema(description = "학교 이메일 형식", defaultValue = "ajou.ac.kr")
    private String schoolEmailForm;

    public SchoolNoIdDto(School school) {
        this.schoolName = school.getSchoolName();
        this.schoolEmailForm = school.getEmailForm();
    }
}
