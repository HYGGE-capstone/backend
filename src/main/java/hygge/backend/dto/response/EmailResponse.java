package hygge.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponse {
    @Schema(description = "이메일", defaultValue = "testEmail@ajou.ac.kr")
    private String email;
}
