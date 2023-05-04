package hygge.backend.dto.request;

import hygge.backend.entity.Member;
import hygge.backend.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@ToString
public class SignupRequest {

    @Schema(description = "로그인 아이디", defaultValue = "testId")
    private String loginId;

    @Schema(description = "이메일", defaultValue = "testId@ajou.ac.kr")
    private String email;

    @Schema(description = "비밀번호", defaultValue = "12345678")
    private String password;

    @Schema(description = "닉네임", defaultValue = "tester")
    private String nickname;

    @Builder
    public SignupRequest(String loginId, String email, String password, String nickname) {
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();
    }

}
