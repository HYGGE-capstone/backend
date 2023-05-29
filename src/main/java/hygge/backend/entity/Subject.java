package hygge.backend.entity;

import hygge.backend.dto.subject.SubjectDto;
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
public class Subject {

    @Id @GeneratedValue
    @Column(name = "SUBJECT_ID")
    private Long id;

    @Column(name = "SUBJECT_NAME")
    private String name;

    private String code;

    private int year;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    @Column(name = "P_NAME")
    private String pName;  // 교수명

    private String time;  // 수업시간 ex) 월A 수B

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHOOL_ID")
    private School school;

    @OneToMany(mappedBy = "subject")
    private List<Team> teams = new ArrayList<>();

    @Builder
    public Subject(Long id, String name, String code, int year, Semester semester, String pName, String time, School school) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.year = year;
        this.semester = semester;
        this.pName = pName;
        this.time = time;
        this.school = school;
    }

    public void change(SubjectDto subjectDto) {
        this.name = subjectDto.getName();
        this.code = subjectDto.getCode();
        this.year = subjectDto.getYear();
        this.semester = subjectDto.getSemester();
        this.pName = subjectDto.getPName();
        this.time = subjectDto.getTime();
    }
}
