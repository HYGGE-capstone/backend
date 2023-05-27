package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School {

    @Id @GeneratedValue
    @Column(name = "SCHOOL_ID")
    private Long id;

    @Column(name = "SCHOOL_NAME")
    private String schoolName;

    @Column(name = "EMAIL_FORM")
    private String emailForm;

    @OneToMany(mappedBy = "school")
    private List<Member> members = new ArrayList<>();

    @Builder
    public School(Long id, String schoolName, String emailForm) {
        this.id = id;
        this.schoolName = schoolName;
        this.emailForm = emailForm;
    }

    //--
    public void changeSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void changeEmailForm(String emailForm) {
        this.emailForm = emailForm;
    }
}
