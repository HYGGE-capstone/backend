package hygge.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
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

    @OneToMany(mappedBy = "member")
    private List<Resume> resumes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Subscribe> subscribes = new ArrayList<>();

    // 빌더
    @Builder
    public Member(String loginId, String email, String password, String nickname, Role role) {
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    // 비즈니스 메서드
    public void addSubscribe(Subscribe subscribe) {
        this.subscribes.add(subscribe);
    }

    public void deleteSubscribe(Subscribe subscribe) {
        this.subscribes.remove(subscribe);
    }
}
