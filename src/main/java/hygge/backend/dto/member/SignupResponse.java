package hygge.backend.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupResponse {

    @Schema(description = "로그인 아이디", defaultValue = "testId")
    private String loginId;

    @Schema(description = "이메일", defaultValue = "testEmail@ajou.ac.kr")
    private String email;
}
