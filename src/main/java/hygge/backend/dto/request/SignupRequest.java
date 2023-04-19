package hygge.backend.dto.request;

import hygge.backend.entity.Member;
import hygge.backend.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SignupRequest {

    private String loginId;

    private String email;

    private String password;

    private String nickname;

    @Builder
    public SignupRequest(String loginId, String email, String password, String nickname) {
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Member toMember() {
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();
    }

}
