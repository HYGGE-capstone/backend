package hygge.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginIdResponse {

    @Schema(description = "로그인 아이디", defaultValue = "testId")
    private String loginId;
}
