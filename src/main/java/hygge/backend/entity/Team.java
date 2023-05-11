package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "TEAM_NAME")
    private String name;


    private String title;

    private String description;

    @Column(name = "MAX_MEMBER")
    private int maxMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEADER_ID")
    private Member leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJECT_ID")
    private Subject subject;


    @Builder
    public Team(String name, String title, String description, int maxMember, Member leader, Subject subject) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.leader = leader;
        this.maxMember = maxMember;
    }

}
