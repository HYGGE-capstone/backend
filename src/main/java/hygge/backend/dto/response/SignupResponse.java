package hygge.backend.dto.response;

import lombok.Data;

@Data
public class SignupResponse {
    private String loginId;

    private String email;
}
