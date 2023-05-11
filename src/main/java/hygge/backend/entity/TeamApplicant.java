package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamApplicant {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_APPLICANT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICANT_ID")
    private Member applicant;

    @Builder
    public TeamApplicant(Team team, Member applicant) {
        this.team = team;
        this.applicant = applicant;
    }
}
