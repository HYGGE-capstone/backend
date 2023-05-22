package hygge.backend.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponse {

    @Schema(description = "회원 아이디", defaultValue = "testId")
    private String memberId;
}
