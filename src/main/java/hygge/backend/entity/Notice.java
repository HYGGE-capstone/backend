package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {
    @Id
    @GeneratedValue
    @Column(name = "NOTICE_ID")
    private Long id;

    @Column(name = "NOTICE_CONTENT")
    private String content;

    @OneToOne
    private Team team;
}
