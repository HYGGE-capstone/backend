package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToOne(mappedBy = "notice")
    private Team team;

    @Builder
    public Notice(String content) {
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
