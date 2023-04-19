package hygge.backend.dto;

import lombok.Data;

@Data
public class SignupResponse {
    private String loginId;

    private String email;
}
