package hygge.backend.entity;

import lombok.Getter;

import javax.persistence.*;


@Entity
@Getter
public class Resume {

    @Id
    @GeneratedValue
    @Column(name = "RESUME_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String content;

    @OneToOne
    @JoinColumn(name = "SUBJECT_ID")
    private Subject subject;
}
