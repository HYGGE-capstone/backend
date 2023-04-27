package hygge.backend.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String loginId;
    private String password;
}
