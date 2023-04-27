package hygge.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHOOL_ID")
    private School school;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // 역할 [USER, ADMIN]

    private String nickname;


    private String refreshToken;

    @OneToMany(mappedBy = "member")
    private List<Resume> resumes = new ArrayList<>();

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}
