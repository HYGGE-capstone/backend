package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(name = "NUM_MEMBER")
    private int numMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEADER_ID")
    private Member leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJECT_ID")
    private Subject subject;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "NOTICE_ID")
    private Notice notice;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamApplicant> teamApplicants = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<MemberTeam> memberTeams = new ArrayList<>();


    @Builder
    public Team(String name, String title, String description, int maxMember, int numMember, Member leader, Subject subject) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.leader = leader;
        this.maxMember = maxMember;
        this.numMember = numMember;
    }

    public void joinMember() {
        this.numMember += 1;
    }
    public void postNotice(Notice notice) { this.notice = notice;}

    public void deleteNotice() { this.notice = null; }

}
