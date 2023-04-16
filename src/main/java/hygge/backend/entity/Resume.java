package hygge.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
public class Resume {

    @Id
    @GeneratedValue
    @Column(name = "RESUME_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String content;

    @OneToOne
    @JoinColumn(name = "SUBJECT_ID")
    private Subject subject;
}
