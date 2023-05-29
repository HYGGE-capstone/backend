package hygge.backend.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    @Schema(description = "에러 코드", defaultValue = "에러를 설명하는 코드")
    private final String code;

    @Schema(description = "에러 메시지", defaultValue = "에러를 설명하는 메시지")
    private final String message;
}
